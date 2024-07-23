package com.mkc.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.mkc.api.common.exception.ErrMonitorCode;
import com.mkc.api.monitor.DdMonitorMsgUtil;
import com.mkc.common.constant.RedisKey;
import com.mkc.common.core.redis.RedisCache;
import com.mkc.common.core.text.Convert;
import com.mkc.common.enums.MerAmountType;
import com.mkc.common.enums.MerSettleType;
import com.mkc.common.utils.DateUtils;
import com.mkc.domain.MerAmountRecord;
import com.mkc.domain.MerInfo;
import com.mkc.domain.MerReqLog;
import com.mkc.mapper.MerInfoMapper;
import com.mkc.process.IMailProcess;
import com.mkc.service.IMerAmountRecordService;
import com.mkc.service.IMerInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 商户信息管理Service业务层处理
 *
 * @author atd
 * @date 2023-04-24
 */
@Slf4j
@Service
@DS("business")
@CacheConfig(cacheNames = RedisKey.MERINFO_KEY)
public class MerInfoServiceImpl implements IMerInfoService {
	@Autowired
	private MerInfoMapper merInfoMapper;

	@Autowired
	private RedisCache redisCache;

	@Autowired
	private IMailProcess mailProcess;

	@Autowired
	private IMerAmountRecordService merAmountRecordService;

	/**
	 * 查询商户信息管理
	 *
	 * @param id 商户信息管理主键
	 * @return 商户信息管理
	 */
	@Override
	public MerInfo selectMerInfoById(Long id) {
		return merInfoMapper.selectMerInfoById(id);
	}

	@Override
	@Cacheable(unless="#result == null")
	public MerInfo selectMerInfoByCode(String code) {
		if(StringUtils.isBlank(code)) {
			return null;
		}
		
		return merInfoMapper.selectMerInfoByCode(code);
	}

	@Override
	public MerInfo selectMerInfoByCodeApi(String code) {

		String key = RedisKey.MERINFO_API_KEY + code;

		MerInfo merInfo = redisCache.getCacheObject(key);
		if (merInfo != null) {
			return merInfo;
		}

		merInfo = reloadMer(code);

		return merInfo;
	}

	/**
	 * 从数据库中加载 商户信息
	 * 
	 * @param merCode 商户code
	 * @return
	 */
	private MerInfo reloadMer(String merCode) {
		String key = RedisKey.MERINFO_API_KEY + merCode;

        log.info("==== reloadMer merCode : {}",merCode);
        MerInfo merInfo=merInfoMapper.selectMerInfoByCode(merCode);
        if (merInfo == null) {
            return null;
        }
        merInfo.setBalanceFlag(ckBalance(merInfo));
        redisCache.setCacheObject(key,merInfo,RedisKey.DEFAULT_OUTTIME);

        String merBalanceKey=RedisKey.MER_BALANCE_KEY+merCode;
        //将余额放入缓存中
        redisCache.deleteObject(merBalanceKey);
        redisCache.increment(merBalanceKey,getMerBalance(merInfo).doubleValue());
		log.info("==== reloadMer merCode success : {}",merCode);
        return merInfo;

	}

	/**
	 * 检查商户是否有可用 余额
	 * 
	 * @param merInfo
	 * @return
	 */
	private boolean ckBalance(MerInfo merInfo) {

		if (getMerBalance(merInfo).compareTo(BigDecimal.ZERO) > 0) {
			return true;
		}

		return false;
	}

	/**
	 * 得到商户的实际可用余额
	 * 
	 * @param merInfo
	 * @return
	 */
	private BigDecimal getMerBalance(MerInfo merInfo) {

		// 判断是否是后付付费模式
		if (MerSettleType.H.getCode().equals(merInfo.getSettleType())) {
			// 后付费 可用余额=当前账户余额+后付费授信额度
			return merInfo.getBalance().add(merInfo.getLineOfCredit());
		}

		return merInfo.getBalance();
	}

	/**
	 * 查询商户信息管理列表
	 *
	 * @param merInfo 商户信息管理
	 * @return 商户信息管理
	 */
	@Override
	public List<MerInfo> selectMerInfoList(MerInfo merInfo) {
		return merInfoMapper.selectMerInfoList(merInfo);
	}


	/**
	 * 查询
	 *
	 * @return 商户信息管理
	 */
	@Override
	@Cacheable(cacheNames = RedisKey.MER_KEY, key = "'all'")
	public List<MerInfo> selectMerNameList() {

		return merInfoMapper.selectMerInfoList(null).stream()
				.map(mer -> new MerInfo(mer.getMerCode(),
						mer.getMerName())).collect(Collectors.toList());
	}


	/**
	 * 新增商户信息管理
	 *
	 * @param merInfo 商户信息管理
	 * @return 结果
	 */
	@Override
	@CacheEvict(cacheNames = RedisKey.MER_KEY, key = "'all'")
	public int insertMerInfo(MerInfo merInfo) {
		merInfo.setSignPwd(DigestUtil.md5Hex(getKey().getBytes()));
		merInfo.setSignKey(DigestUtil.md5Hex(getKey().getBytes()));
		merInfo.setBalance(BigDecimal.ZERO);
		merInfo.setCreateTime(DateUtils.getNowDate());
		merInfo.setUpdateTime(DateUtils.getNowDate());
		return merInfoMapper.insert(merInfo);
		// return merInfoMapper.insertMerInfo(merInfo);
	}

	private String getKey() {
		return UUID.randomUUID().toString();
	}

	/**
	 * 修改商户信息管理
	 *
	 * @param merInfo 商户信息管理
	 * @return 结果
	 */
	@Override
	public int updateMerInfo(MerInfo merInfo) {
		merInfo.setUpdateTime(DateUtils.getNowDate());
		int i = merInfoMapper.updateMerInfo(merInfo);
		if (i <= 0) {
			return i;
		}
		cleanMerInfoCache(merInfo.getId(),merInfo.getMerCode());
		return i;
	}

	/**
	 * 批量删除商户信息管理
	 *
	 * @param ids 需要删除的商户信息管理主键
	 * @return 结果
	 */
	@Override
	@CacheEvict(cacheNames = RedisKey.MER_KEY, key = "'all'")
	public int deleteMerInfoByIds(String ids) {
		return merInfoMapper.deleteMerInfoByIds(Convert.toIntArray(ids));
	}

	/**
	 * 删除商户信息管理信息
	 *
	 * @param id 商户信息管理主键
	 * @return 结果
	 */
	@Override
	@CacheEvict(cacheNames = RedisKey.MER_KEY, key = "'all'")
	public int deleteMerInfoById(Long id) {
		return merInfoMapper.deleteMerInfoById(id);
	}

	@Override
	public void dedMerBalance_SYS( MerReqLog merLog) {

		String merCode = merLog.getMerCode();
		BigDecimal orderAmount = merLog.getSellPrice();
		try {
			int num = merInfoMapper.dedMerBalance(merCode, orderAmount);

			if (num != 1) {
				log.error(ErrMonitorCode.BIZ_ERR + " ========扣减商户余额失败 merCode {},  orderAmount {} ", merCode,
						orderAmount);
			}

			String merBalanceKey = RedisKey.MER_BALANCE_KEY + merCode;

			double merBalance = redisCache.increment(merBalanceKey, orderAmount.negate().doubleValue());
			// 判断账户余额是否不足
			if (merBalance <= 0) {
				reloadMer(merCode);
			}
			log.info("=== 商户余额扣减  merCode {} , orderNo {} , orderAmount {} , 余额:merBalance {} ",merCode,merLog.getOrderNo(),orderAmount,merBalance);
			//TODO 考虑数据库磁盘存储空间 暂时不记录 扣费记录
			//MerAmountRecord record=new MerAmountRecord(merCode,merLog.getMerName(),orderAmount,
			//merLog.getOrderNo(),merLog.getCreateTime());
			//record.setType(MerAmountType.SYS_DISCOUNT.getCode());
			//record.setRemark("====================");
			//saveMerAmountRecord(record, "system");

			MerInfo merInfo = selectMerInfoByCodeApi(merCode);
			BigDecimal warnAmount = merInfo.getWarnAmount();
			//判断当前商户账户余额是否小于等于 预警金额 && 预警金额大于 0
			if(warnAmount.compareTo(BigDecimal.ZERO) >=0 && warnAmount.compareTo(new BigDecimal(merBalance))>=0){
				String merBalanceWarnKey = RedisKey.MER_BALANCE_WARN_KEY + merCode;
				Integer remainSecondsOneDay = DateUtils.getRemainSecondsOneDay(new Date());
				//判断今天是否已经提醒过
				boolean flag=redisCache.setIfAbsent(merBalanceWarnKey,merCode+merBalance,remainSecondsOneDay, TimeUnit.SECONDS);
				if(flag){
					DdMonitorMsgUtil.sendDdBusinessMsg("【温馨提示】商户【 {} - {}】 账户余额已达到预警金额，当前余额为 {} ，请知悉处理！",merInfo.getMerName(),merInfo.getMerCode(),merInfo.getBalance());
				}
			}

		} catch (Exception e) {
			log.error(ErrMonitorCode.BIZ_ERR + " ========扣减商户余额异常 merCode {} ,  orderAmount {} , err {}", merCode,
					orderAmount, e);
		}

	}

    @Override
	@Transactional(rollbackFor = Exception.class)
    public int updMerBalance(String merCode, BigDecimal orderAmount, MerAmountType type,String updBy,String remark){

        int num=0;
        try {
            MerInfo merInfo = this.selectMerInfoByCode(merCode);
            if (merInfo==null){
                return num;
            }
			BigDecimal beforeAmount=merInfo.getBalance();
            MerAmountRecord record=new MerAmountRecord(merCode,orderAmount);
			BigDecimal afterAmount=BigDecimal.ZERO;
			if(MerAmountType.ADD.getCode().equals(type.getCode())){
				num=merInfoMapper.addMerBalance(merCode,orderAmount);
				afterAmount = beforeAmount.add(orderAmount);

			}else if(MerAmountType.DISCOUNT.getCode().equals(type.getCode())) {
				num=merInfoMapper.dedMerBalance(merCode,orderAmount);
				afterAmount = beforeAmount.subtract(orderAmount);
            }
            if (num != 1) {
                log.error(ErrMonitorCode.BIZ_ERR+" ========更新 扣减商户余额失败 merCode {},  orderAmount {} ", merCode, orderAmount);
            }

            String merBalanceKey=RedisKey.MER_BALANCE_KEY+merCode;
            double merBalance = redisCache.increment(merBalanceKey, orderAmount.doubleValue());
            //判断账户余额是否不足
            if(merBalance <= 0){
                String merInfoKey= RedisKey.MERINFO_API_KEY+merCode;
                reloadMer(merCode);
            }
            record.setType(type.getCode());
            record.setRemark(remark);
            record.setMerName(merInfo.getMerName());
			record.setBeforeAmount(beforeAmount);
			record.setAfterAmount(afterAmount);
            //保存操作记录
            saveMerAmountRecord(record,updBy);

			reloadMer(merCode);
			return  num;
        }catch (Exception e){
            log.error(ErrMonitorCode.BIZ_ERR+" ========更新 扣减商户余额异常 merCode {} ,  orderAmount {} , err {}", merCode, orderAmount,e);
			return 0;
        }

    }

	@Override
	public void sendMerKeyMail(Long id) {
		MerInfo merInfo = merInfoMapper.selectMerInfoById(id);

		mailProcess.sendMerKey(merInfo);

	}


	private void saveMerAmountRecord( MerAmountRecord record,String createBy){

        record.setCreateBy(createBy);
        record.setUpdateBy(createBy);
        record.setCreateTime(new Date());
        record.setUpdateTime(new Date());
        merAmountRecordService.insertMerAmountRecord(record);
    }

    /**
     * 清除缓存
     * @param merCode
     */
    private void cleanMerInfoCache(long merId,String merCode) {
		if (StringUtils.isBlank(merCode)) {
			MerInfo merInfo = merInfoMapper.selectMerInfoById(merId);
			if(merInfo==null){
				return ;
			}
			merCode=merInfo.getMerCode();
		}
		log.info("开始清除商户缓存 merCode  {}",merCode);

        String apiKey= RedisKey.MERINFO_API_KEY+merCode;
        String key= RedisKey.MERINFO_KEY+merCode;
        redisCache.deleteObject(apiKey);
        redisCache.deleteObject(key);

      //  String merBalanceKey=RedisKey.MER_BALANCE_KEY+merCode;
        //将缓存中的余额进行删除
     //   redisCache.deleteObject(merBalanceKey);
    }
}
