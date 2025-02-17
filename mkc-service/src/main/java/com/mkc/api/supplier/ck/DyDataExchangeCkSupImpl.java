package com.mkc.api.supplier.ck;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.jayway.jsonpath.JsonPath;
import com.mkc.api.common.constant.bean.SupResult;
import com.mkc.api.common.exception.ApiServiceException;
import com.mkc.api.dto.ck.req.ProQualifyCertReqDTO;
import com.mkc.api.dto.ck.res.ProQualifyCertResDTO;
import com.mkc.api.supplier.ICkSupService;
import com.mkc.api.supplier.ck.mapper.DyDataExchangeCkSupMapper;
import com.mkc.api.supplier.dto.ProQualifyCertSupResDTO;
import com.mkc.bean.SuplierQueryBean;
import com.mkc.common.core.redis.RedisCache;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 德阳数据交易科技有限公司
 *
 * @author chentong
 * @version 1.0
 * @description: description
 * @date 2024/12/17 17:18
 */
@Service("CK_DYSJJY")
@Slf4j
public class DyDataExchangeCkSupImpl implements ICkSupService {

	@Resource
	private DyDataExchangeCkSupMapper dyDataExchangeCkSupMapper;

	@Resource
	private RedisCache redisCache;


	public List<String> successCodes() {
		return ListUtil.list(false, "200");
	}

	public List<String> errorCodes() {
		return ListUtil.list(false, "500");
	}

	public List<String> notFoundCodes() {
		return ListUtil.list(false);
	}

	@Override
	public SupResult<List<ProQualifyCertResDTO>> ckProQualifyCert(ProQualifyCertReqDTO dto, SuplierQueryBean bean) {

		LocalDateTime reqTime = LocalDateTime.now();
		String appToken = requestAppToken(bean);
		if (CharSequenceUtil.isBlank(appToken)) {
			return SupResult.supAuthErr();
		}
		String url = bean.getUrl() + CharSequenceUtil.format("/dc-dbapi/api/getApi170373136882636?name={}&IdNum={}", dto.getName(), dto.getIdNum());

		SupResult<List<ProQualifyCertResDTO>> supResult = new SupResult<>(url, reqTime);
		// todo 供应商还未确定Authorization授权文件怎么传，确定了再改，暂时不传
		String body = "";
		try {
			try (HttpResponse response = HttpUtil.createPost(url)
					.header("app-token", appToken)
					.execute()) {
				LocalDateTime resTime = LocalDateTime.now();
				if (response.getStatus() == 200) {
					body = response.body();
					Response res = JSONUtil.toBean(body, Response.class);
					String code = res.getCode();
					String message = res.getMessage();
					List<ProQualifyCertSupResDTO> list = res.getData();
					if (successCodes().contains(code)) {
						List<ProQualifyCertResDTO> collect = list.stream()
								.map(supRes -> dyDataExchangeCkSupMapper.supRes2Res(supRes))
								.collect(Collectors.toList());
						supResult.success(resTime, collect);
					}
					if (errorCodes().contains(code)) {
						supResult.error(resTime, message);
					}
					if (notFoundCodes().contains(code)) {
						supResult.notFound(resTime);
					}
				} else {
					return supResult.error(resTime, body);
				}
				return supResult;
			}
		} catch (Throwable e) {
			errMonitorMsg(log, " 【{}】 技能人员职业资格证书核验数据元件接口 发生异常 orderNo {} URL {} , 报文: {} , err {}",
					bean.getSupName(), bean.getOrderNo(), url, body, e);
			return supResult.error(LocalDateTime.now(), CharSequenceUtil.format("异常：{}", e.getMessage()));
		}
	}

	public String requestAppToken(SuplierQueryBean bean) {

		String hkey = "sup_auth_token";
		String key = CharSequenceUtil.format("DYSJJY_auth_token:{}", bean.getAuthAccount());

		String appToken = redisCache.getCacheMapValue(key, hkey);
		if (CharSequenceUtil.isBlank(appToken)) {
			AuthReqDTO authReq = AuthReqDTO.builder()
					.appkey(bean.getSignKey())
					.input(bean.getAuthAccount())
					.password(bean.getAuthPwd())
					.build();
			String url = bean.getUrl() + "/dc-sso/componentToken/generateAppToken";
			try (HttpResponse response = HttpUtil.createPost(url)
					.body(JSONUtil.toJsonStr(authReq))
					.header("Content-Type", "application/json")
					.execute()) {
				if (response.getStatus() == 200) {
					String code = String.valueOf(JsonPath.<Object>read(response.body(), "$.code"));
					if (successCodes().contains(code)) {
						appToken = JsonPath.read(response.body(), "$.data");
					} else {
						String message = JsonPath.read(response.body(), "$.message");
						throw new ApiServiceException(CharSequenceUtil.format("供应商app-token获取失败: {}", message));
					}
				} else {
					throw new ApiServiceException(CharSequenceUtil.format("供应商app-token获取失败: {}", response.body()));
				}
			}
			redisCache.setCacheMapValue(key, hkey, appToken, 12L, TimeUnit.HOURS);
		}
		return appToken;
	}

	@Data
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	private static class AuthReqDTO {
		private String appkey;

		private String input;

		private String password;
	}


	@Data
	private static class Response {
		private String code;
		private String message;
		private List<ProQualifyCertSupResDTO> data;
	}
}
