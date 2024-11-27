package com.mkc.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.mkc.api.common.constant.ProductPrivacyKey;
import com.mkc.api.common.utils.ApiUtils;
import com.mkc.common.utils.DateUtils;
import com.mkc.common.utils.StringUtils;
import com.mkc.common.utils.ZipStrUtils;
import com.mkc.domain.*;
import com.mkc.service.IMerInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author tqlei
 * @date 2023/6/30 14:53
 */

@Slf4j
@RestController
@RequestMapping("/test")
@Profile({"dev", "local", "test", "pre"})
public class TestController {


	@Autowired
	private IMerInfoService merchantService;


	/**
	 * 车五项
	 */
	@PostMapping("/testCar")
	public void testCar(MultipartFile excel, int sheetNo, HttpServletResponse response) {
		try {
			List<ExcelTestCar> readList = EasyExcel.read(excel.getInputStream())
					.headRowNumber(1)
					.head(ExcelTestCar.class)
					.sheet(sheetNo - 1)
					.doReadSync();

			for (ExcelTestCar read : readList) {
				JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(read));
				String plaintext = read.getPlateNo();
				JSONObject post = ApiUtils.queryApi("http://api.zjbhsk.com/bg/carInfo", jsonObject, plaintext);
				read.setCode(post.getString("code"));
				read.setMsg(post.getString("msg"));
				try {
					JSONObject data = post.getJSONObject("data");
					if (Objects.nonNull(data)) {
						read.setEngineNo(data.getString("engineNo"));
						read.setBrandName(data.getString("brandName"));
						read.setVin(data.getString("vin"));
						read.setInitialRegistrationDate(data.getString("initialRegistrationDate"));
						read.setModelNo(data.getString("modelNo"));
					}
				} catch (Exception e) {
					read.setEngineNo(post.getString("data"));
					log.error(e.getMessage());
				}
			}
			log.info(readList.size() + "条样例测试完毕！");
			setExcelRespProp(response, DateUtils.dateTimeNow() + "车五项测试结果");
			EasyExcel.write(response.getOutputStream())
					.head(ExcelTestCar.class)
					.excelType(ExcelTypeEnum.XLSX)
					.sheet("车五项测试结果" + sheetNo)
					.doWrite(readList);
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}

	/**
	 * 高等学历-法信
	 */
	@PostMapping("/testHouse")
	public void testHouse(@RequestBody MultipartFile excel, Integer sheetNo, HttpServletResponse response) {
		try {
			List<ExcelTestHouse> readList = EasyExcel.read(excel.getInputStream())
					.headRowNumber(1)
					.head(ExcelTestHouse.class)
					.sheet(sheetNo - 1)
					.doReadSync();
			ArrayList<ExcelTestHouse> writeList = new ArrayList<>();
			for (ExcelTestHouse read : readList) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("reqOrderNo", read.getReqOrderNo());
				jsonObject.put("personCardNumList", Collections.singletonList(read.getPersonCardNum()));
				String plaintext = read.getReqOrderNo();
				JSONObject post = ApiUtils.queryApi("http://api.zjbhsk.com/bg/houseResultReqInfo", jsonObject, plaintext);
				try {
					JSONObject data = post.getJSONObject("data");
					if (Objects.nonNull(data)) {
						JSONArray authResults = data.getJSONArray("authResults");
						if (!CollectionUtils.isEmpty(authResults)) {
							JSONObject authResult = authResults.getJSONObject(0);
							JSONArray resultList = authResult.getJSONArray("resultList");
							if (!CollectionUtils.isEmpty(resultList)) {
								for (int i = 0; i < resultList.size(); i++) {
									JSONObject result = resultList.getJSONObject(i);
									ExcelTestHouse excelTestHouse = new ExcelTestHouse();
									excelTestHouse.setXm(read.getXm());
									excelTestHouse.setPersonCardNum(read.getPersonCardNum());
									excelTestHouse.setCode(post.getString("code"));
									excelTestHouse.setReqOrderNo(read.getReqOrderNo());
									excelTestHouse.setCertNo(result.getString("certNo"));
									excelTestHouse.setUnitNo(result.getString("unitNo"));
									excelTestHouse.setLocation(result.getString("location"));
									excelTestHouse.setOwnership(result.getString("ownership"));
									excelTestHouse.setHouseArea(result.getString("houseArea"));
									excelTestHouse.setRightsType(result.getString("rightsType"));
									excelTestHouse.setIsSealUp(result.getString("isSealUp"));
									excelTestHouse.setIsMortgaged(result.getString("isMortgaged"));
									excelTestHouse.setRightsStartTime(result.getString("rightsStartTime"));
									excelTestHouse.setRightsEndTime(result.getString("rightsEndTime"));
									excelTestHouse.setUseTo(result.getString("useTo"));
									writeList.add(excelTestHouse);
								}
							} else {
								ExcelTestHouse excelTestHouse = new ExcelTestHouse();
								excelTestHouse.setXm(read.getXm());
								excelTestHouse.setPersonCardNum(read.getPersonCardNum());
								excelTestHouse.setCode(post.getString("code"));
								excelTestHouse.setReqOrderNo(read.getReqOrderNo());
								writeList.add(excelTestHouse);
							}
						}
					}
				} catch (Exception e) {
					read.setCertNo(post.toJSONString());
					log.error(e.getMessage());
				}
				System.err.println(post);
			}
			setExcelRespProp(response, DateUtils.dateTimeNow() + "不动产测试结果");
			EasyExcel.write(response.getOutputStream())
					.head(ExcelTestHouse.class)
					.excelType(ExcelTypeEnum.XLSX)
					.sheet("不动产测试结果")
					.doWrite(writeList);
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}

	public static void main(String[] args) {
		JSONObject jsonObject = JSON.parseObject(" {\"merCode\":\"BAIHANG01\",\"ranStr\":\"0d84gc1ekoc30mp3o99h36ajwsckxlsf\",\"certNo\":\"6vpS/7h7sRcIu6C4mZMOjOh2PnJlpMVEIY57NcPNx50=\",\"creditCode\":\"91500108599207496B\",\"companyName\":\"重庆市爱平眼镜店\",\"legalPerson\":\"钟云亮\"}");
		jsonObject = ProductPrivacyKey.privacyDecryptMer(jsonObject);
		System.err.println(jsonObject);
	}

	/**
	 * 社保经济能力评级V3
	 */
	@PostMapping("/testV3")
	public void testEconomicRate(MultipartFile excel, int sheetNo, HttpServletResponse response) {
		try {
			List<ExcelTestV> readList = EasyExcel.read(excel.getInputStream())
					.headRowNumber(1)
					.head(ExcelTestV.class)
					.sheet(sheetNo - 1)
					.doReadSync();

			for (ExcelTestV read : readList) {
				JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(read));
				String plaintext = read.getIdCard() + read.getName() + read.getMobile();
				JSONObject post = ApiUtils.queryApi("http://api.zjbhsk.com/bg/financeInfoV3", jsonObject, plaintext);
				read.setCode(post.getString("code"));
				try {
					JSONObject data = post.getJSONObject("data");
					if (Objects.nonNull(data)) {
						read.setLevel(data.getString("level"));
					}
				} catch (Exception e) {
					read.setLevel(post.getString("data"));
					log.error(e.getMessage());
				}
			}
			log.info(readList.size() + "条样例测试完毕！");
			setExcelRespProp(response, DateUtils.dateTimeNow() + "经济能力评级V3测试结果");
			EasyExcel.write(response.getOutputStream())
					.head(ExcelTestV.class)
					.excelType(ExcelTypeEnum.XLSX)
					.sheet("经济能力评级V3测试结果" + sheetNo)
					.doWrite(readList);
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}

	/**
	 * 社保经济能力评级V7
	 */
	@PostMapping("/testV7")
	public void testV7(MultipartFile excel, int sheetNo, HttpServletResponse response) {
		try {
			List<ExcelTestV> readList = EasyExcel.read(excel.getInputStream())
					.headRowNumber(1)
					.head(ExcelTestV.class)
					.sheet(sheetNo - 1)
					.doReadSync();

			for (ExcelTestV read : readList) {
				JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(read));
				String plaintext = read.getIdCard() + read.getName() + read.getMobile();
				JSONObject post = ApiUtils.queryApi("http://api.zjbhsk.com/bg/financeInfoV7", jsonObject, plaintext);
				read.setCode(post.getString("code"));
				try {
					JSONObject data = post.getJSONObject("data");
					if (Objects.nonNull(data)) {
						read.setLevel(data.getString("level"));
					}
				} catch (Exception e) {
					read.setLevel(post.getString("data"));
					log.error(e.getMessage());
				}
			}
			log.info(readList.size() + "条样例测试完毕！");
			setExcelRespProp(response, DateUtils.dateTimeNow() + "经济能力评级V7测试结果");
			EasyExcel.write(response.getOutputStream())
					.head(ExcelTestV.class)
					.excelType(ExcelTypeEnum.XLSX)
					.sheet("经济能力评级V7测试结果" + sheetNo)
					.doWrite(readList);
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}

	/**
	 * 社保经济能力2W
	 */
	@PostMapping("/test2W")
	public void test2W(@RequestBody MultipartFile excel, HttpServletResponse response) {
		try {
			List<ExcelTest2W> readList = EasyExcel.read(excel.getInputStream())
					.headRowNumber(1)
					.head(ExcelTest2W.class)
					.sheet(0)
					.doReadSync();

			for (ExcelTest2W read : readList) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("idCard", read.getIdCard());
				jsonObject.put("name", read.getName());
				jsonObject.put("mobile", read.getMobile());
				String plainText = read.getIdCard() + read.getName() + read.getMobile();
				JSONObject post = ApiUtils.queryApi("http://api.zjbhsk.com/bg/financeInfo", jsonObject, plainText);
				read.setCode(post.getString("code"));
				try {
					JSONObject data = post.getJSONObject("data");
					if (Objects.nonNull(data)) {
						read.setRange(data.getString("range"));
						read.setHistory(data.getString("history"));
						read.setStability(data.getString("stability"));
					}
				} catch (Exception e) {
					read.setRange(post.getString("data"));
					log.error(e.getMessage());
				}
			}
			log.info(readList.size() + "条样例测试完毕！");
			setExcelRespProp(response, DateUtils.dateTimeNow() + "社保经济能力2W测试结果");
			EasyExcel.write(response.getOutputStream())
					.head(ExcelTest2W.class)
					.excelType(ExcelTypeEnum.XLSX)
					.sheet("社保经济能力2W测试结果")
					.doWrite(readList);
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}

	/**
	 * 企业四要素
	 */
	@PostMapping("/testEnterpriseFour")
	public void testEnterpriseFour(@RequestBody MultipartFile excel, HttpServletResponse response) {
		try {
			List<ExcelEnterpriseFour> readList = EasyExcel.read(excel.getInputStream())
					.headRowNumber(1)
					.head(ExcelEnterpriseFour.class)
					.sheet(0)
					.doReadSync();

			for (ExcelEnterpriseFour read : readList) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("companyName", read.getCompanyName());
				jsonObject.put("creditCode", StringUtils.isNotBlank(read.getCreditCode()) ? read.getCreditCode() : read.getRegistrationNumber());
				jsonObject.put("legalPerson", read.getLegalPerson());
				jsonObject.put("certNo", read.getCertNo());
				String plainText = jsonObject.getString("companyName") + jsonObject.getString("creditCode") + jsonObject.getString("legalPerson") + jsonObject.getString("certNo");
				JSONObject post = ApiUtils.queryApi("http://api.zjbhsk.com/bg/enterpriseFourElementInfo", jsonObject, plainText);
				read.setCode(post.getString("code"));
				try {
					JSONObject data = post.getJSONObject("data");
					if (Objects.nonNull(data)) {
						read.setReturnCode(data.getString("returnCode"));
						read.setCreditCodeMatch(data.getString("creditCodeMatch"));
						read.setCompanyNameMatch(data.getString("companyNameMatch"));
						read.setLegalPersonMatch(data.getString("legalPersonMatch"));
						read.setCertNoMatch(data.getString("certNoMatch"));
					}
				} catch (Exception e) {
					read.setReturnCode(post.getString("data"));
					log.error(e.getMessage());
				}
			}
			log.info(readList.size() + "条样例测试完毕！");
			setExcelRespProp(response, DateUtils.dateTimeNow() + "企业四要素测试结果");
			EasyExcel.write(response.getOutputStream())
					.head(ExcelEnterpriseFour.class)
					.excelType(ExcelTypeEnum.XLSX)
					.sheet("企业四要素测试结果")
					.doWrite(readList);
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}


	/**
	 * 行驶证核验
	 */
	@PostMapping("/testDriving")
	public void testDriving(@RequestBody MultipartFile excel, HttpServletResponse response) {
		try {
			List<ExcelTestDriving> readList = EasyExcel.read(excel.getInputStream())
					.headRowNumber(1)
					.head(ExcelTestDriving.class)
					.sheet(0)
					.doReadSync();

			for (ExcelTestDriving read : readList) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("name", read.getName());
				jsonObject.put("licensePlateNo", read.getLicensePlateNo());
				jsonObject.put("plateType", read.getPlateType());
				String plainText = read.getName() + read.getLicensePlateNo() + read.getPlateType();
				JSONObject post = ApiUtils.queryApi("http://api.zjbhsk.com/bg/drivingLicense", jsonObject, plainText);
				read.setCode(post.getString("code"));
				try {
					JSONObject data = post.getJSONObject("data");
					if (Objects.nonNull(data)) {
						JSONObject entity = data.getJSONObject("entity");
						if (Objects.nonNull(entity)) {
							read.setCrossWeight(entity.getString("crossWeight"));
							read.setColor(entity.getString("color"));
							read.setFuel(entity.getString("fuel"));
							read.setWheelBase(entity.getString("wheelBase"));
							read.setEngineType(entity.getString("engineType"));
							read.setPlate(entity.getString("plate"));
							read.setType(entity.getString("type"));
							read.setRetirement(entity.getString("retirement"));
							read.setResult(entity.getString("result"));
							read.setEngine(entity.getString("engine"));
							read.setVariety(entity.getString("variety"));
							read.setRecord(entity.getString("record"));
							read.setVin(entity.getString("vin"));
							read.setMan(entity.getString("man"));
							read.setState(entity.getString("state"));
							read.setBrand(entity.getString("brand"));
							read.setVehicleType(entity.getString("vehicleType"));
							read.setCc(entity.getString("cc"));
							read.setMaxJourney(entity.getString("maxJourney"));
							read.setLoadWeight(entity.getString("loadWeight"));
							read.setPassengers(entity.getString("passengers"));
							read.setRearTread(entity.getString("rearTread"));
							read.setJianCheTime(entity.getString("jianCheTime"));
							read.setPps(entity.getString("pps"));
							read.setFrontTread(entity.getString("frontTread"));
							read.setCurbWeight(entity.getString("curbWeight"));
							read.setValidity(entity.getString("validity"));
							read.setProperties(entity.getString("properties"));
							read.setShaft(entity.getString("shaft"));
						}
					}
				} catch (Exception e) {
					read.setCrossWeight(post.getString("data"));
					log.error(e.getMessage());
				}
			}
			log.info(readList.size() + "条样例测试完毕！");
			setExcelRespProp(response, DateUtils.dateTimeNow() + "行驶证核验测试结果");
			EasyExcel.write(response.getOutputStream())
					.head(ExcelTestDriving.class)
					.excelType(ExcelTypeEnum.XLSX)
					.sheet("行驶证核验测试结果")
					.doWrite(readList);
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}


	/**
	 * 高等学历-法信
	 */
	@PostMapping("/testEdu")
	public void testEdu(@RequestBody MultipartFile excel, HttpServletResponse response) {
		try {
			List<ExcelTestEdu> readList = EasyExcel.read(excel.getInputStream())
					.headRowNumber(1)
					.head(ExcelTestEdu.class)
					.sheet(0)
					.doReadSync();

			for (ExcelTestEdu read : readList) {
				JSONObject jsonObject = new JSONObject();
//                jsonObject.put("reqOrderNo", read.getReqOrderNo());
				jsonObject.put("xm", read.getXm());
				jsonObject.put("zsbh", read.getZsbh());
				String plainText = read.getXm() + read.getZsbh();
				JSONObject post = ApiUtils.queryApi("http://api.zjbhsk.com/bg/highSchoolEduResultInfo", jsonObject, plainText);
				read.setCode(post.getString("code"));
				try {
					JSONObject data = post.getJSONObject("data");
					if (Objects.nonNull(data)) {
						read.setResultCode(data.getString("resultCode"));
						read.setReqOrderNo(data.getString("reqOrderNo"));
//                        read.setStatus(data.getString("status"));
//                        read.setIsExists(data.getBoolean("isExists"));
//                        read.setZjbh(data.getString("zjbh"));
//                        read.setYxmc(data.getString("yxmc"));
//                        read.setZymc(data.getString("zymc"));
//                        read.setCc(data.getString("cc"));
//                        read.setRxrq(data.getString("rxrq"));
//                        read.setByrq(data.getString("byrq"));
//                        read.setXxxs(data.getString("xxxs"));
					}
				} catch (Exception e) {
					read.setResultCode(post.getString("data"));
					log.error(e.getMessage());
				}
				System.err.println(post);
			}
			setExcelRespProp(response, DateUtils.dateTimeNow() + "学历测试结果");
			EasyExcel.write(response.getOutputStream())
					.head(ExcelTestEdu.class)
					.excelType(ExcelTypeEnum.XLSX)
					.sheet("学历测试结果")
					.doWrite(readList);
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}


	/**
	 * 高等学历-中电郑州
	 */
	@PostMapping("/testEduZjhm")
	public void testEduZjhm(@RequestBody MultipartFile excel, HttpServletResponse response) {
		try {
			List<ExcelTestEduZjhm> readList = EasyExcel.read(excel.getInputStream())
					.headRowNumber(1)
					.head(ExcelTestEduZjhm.class)
					.sheet(0)
					.doReadSync();

			ArrayList<ExcelTestEduZjhm> resultList = new ArrayList<>();
			for (ExcelTestEduZjhm read : readList) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("xm", read.getXm());
				jsonObject.put("zjhm", read.getZjhm());
				String plainText = read.getXm() + read.getZjhm();
				JSONObject post = ApiUtils.queryApi("http://api.zjbhsk.com/bg/educationInfo", jsonObject, plainText);
				try {
					JSONArray dataList = post.getJSONArray("data");
					for (int i = 0; i < dataList.size(); i++) {
						JSONObject data = dataList.getJSONObject(i);
						ExcelTestEduZjhm excelTestEduZjhm = new ExcelTestEduZjhm();
						excelTestEduZjhm.setCode(post.getString("code"));
						excelTestEduZjhm.setXm(read.getXm());
						excelTestEduZjhm.setZjhm(read.getZjhm());
						excelTestEduZjhm.setYxmc(data.getString("yxmc"));
						excelTestEduZjhm.setZymc(data.getString("zymc"));
						excelTestEduZjhm.setCc(data.getString("cc"));
						excelTestEduZjhm.setRxrq(data.getString("rxrq"));
						excelTestEduZjhm.setByrq(data.getString("byrq"));
						excelTestEduZjhm.setXxxs(data.getString("xxxs"));
						excelTestEduZjhm.setZsbh(data.getString("zhsb"));
						resultList.add(excelTestEduZjhm);
					}
				} catch (Exception e) {
					ExcelTestEduZjhm excelTestEduZjhm = new ExcelTestEduZjhm();
					excelTestEduZjhm.setXm(read.getXm());
					excelTestEduZjhm.setZjhm(read.getZjhm());
					excelTestEduZjhm.setCode(post.getString("code"));
					excelTestEduZjhm.setYxmc(post.getString("msg"));
					resultList.add(excelTestEduZjhm);
					log.error(e.getMessage());
				}
				System.err.println(post);
			}
			setExcelRespProp(response, DateUtils.dateTimeNow() + "学历测试结果");
			EasyExcel.write(response.getOutputStream())
					.head(ExcelTestEduZjhm.class)
					.excelType(ExcelTypeEnum.XLSX)
					.sheet("学历测试结果")
					.doWrite(resultList);
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}


	/**
	 * 日志解压解密
	 */
	@PostMapping("/testLog")
	public void testLog(@RequestBody MultipartFile excel, HttpServletResponse response) {
		try {
			List<ExcelTestLog> readList = EasyExcel.read(excel.getInputStream())
					.headRowNumber(1)
					.head(ExcelTestLog.class)
					.sheet(0)
					.doReadSync();
			for (ExcelTestLog read : readList) {
				//{"certNo":"Is15DRSGsPb3OYLRGI1F2O9Xch7aa95Zli7EQ82ZRt4=","companyName":"比亚迪股份有限公司","creditCode":"440301501127941","legalPerson":"王传福","merCode":"ZD-BIGDATA","merSeq":"P202410151616150001","paramType":"0","sign":"0bcee3d48fe394e12cda44842d05ccb3"}

				try {
					JSONObject jsonObject = JSON.parseObject(read.getQqcs());
					jsonObject = ProductPrivacyKey.privacyDecryptMer(jsonObject);
					read.setQqcs(jsonObject.toJSONString());
					read.setXycs(ZipStrUtils.gunzip(read.getXycs()));
				} catch (Exception e) {
					read.setXycs(read.getXycs());
				}
			}
			setExcelRespProp(response, DateUtils.dateTimeNow() + "日志");
			EasyExcel.write(response.getOutputStream())
					.head(ExcelTestLog.class)
					.excelType(ExcelTypeEnum.XLSX)
					.sheet("日志")
					.doWrite(readList);
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}


	/**
	 * 日志parse
	 */
	@PostMapping("/testLogParse")
	public void testLogParse(MultipartFile excel, HttpServletResponse response) {
		try {
			List<ExcelTestLog> readList = EasyExcel.read(excel.getInputStream())
					.headRowNumber(1)
					.head(ExcelTestLog.class)
					.sheet(0)
					.doReadSync();
			for (ExcelTestLog read : readList) {

//                if (StringUtils.isBlank(read.getMsg())) {
				JSONObject jsonObject = JSON.parseObject(read.getQqcs());
				read.setDdh(jsonObject.getString("requestId"));
//                    String plaintext = read.getPlateNo();
//                    JSONObject post = ApiUtils.queryApi("http://api.zjbhsk.com/bg/carInfo", jsonObject, plaintext);
//                    read.setCode(post.getString("code"));
//                    read.setMsg(post.getString("msg"));
//                    try {
//                        JSONObject data = post.getJSONObject("data");
//                        if (Objects.nonNull(data)) {
//                            read.setEngineNo(data.getString("engineNo"));
//                            read.setBrandName(data.getString("brandName"));
//                            read.setVin(data.getString("vin"));
//                            read.setInitialRegistrationDate(data.getString("initialRegistrationDate"));
//                            read.setModelNo(data.getString("modelNo"));
//                        }
//                    } catch (Exception e) {
//                        read.setEngineNo(post.getString("data"));
//                        log.error(e.getMessage());
//                    }
//
//                }
			}
			setExcelRespProp(response, DateUtils.dateTimeNow() + "日志");
			EasyExcel.write(response.getOutputStream())
					.head(ExcelTestLog.class)
					.excelType(ExcelTypeEnum.XLSX)
					.sheet("日志")
					.doWrite(readList);
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}

	@PostMapping("/testParseMsg")
	public void testParseMsg(MultipartFile excel, HttpServletResponse response) {
		List<ExcelTestCar> excelTestCars = new ArrayList<>();
		try {
			List<ExcelTestLog> readList = EasyExcel.read(excel.getInputStream())
					.headRowNumber(1)
					.head(ExcelTestLog.class)
					.sheet(0)
					.doReadSync();
			for (ExcelTestLog read : readList) {
				String carNo = JSON.parseObject(read.getQqcs()).getString("carNo");
				String msg = StringUtils.EMPTY;
				try {
					msg = JSON.parseObject(ZipStrUtils.gunzip(read.getXycs())).getString("message");
				} catch (Exception e) {
					msg = "重新请求";
				}
				ExcelTestCar excelTestCar = new ExcelTestCar();
				excelTestCar.setPlateNo(carNo);
				excelTestCar.setMsg(msg);
				excelTestCars.add(excelTestCar);
			}
			setExcelRespProp(response, DateUtils.dateTimeNow() + "日志");
			EasyExcel.write(response.getOutputStream())
					.head(ExcelTestLog.class)
					.excelType(ExcelTypeEnum.XLSX)
					.sheet("车五项异常")
					.doWrite(excelTestCars);
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}

	@PostMapping("/testParseCar")
	public void testParseCar(MultipartFile excel, HttpServletResponse response) {
		List<ExcelTestCar> excelTestCars = new ArrayList<>();
		try {
			for (int i = 0; i < 5; i++) {
				List<ExcelTestCar> readList = EasyExcel.read(excel.getInputStream())
						.headRowNumber(1)
						.head(ExcelTestCar.class)
						.sheet(i)
						.doReadSync();
				for (ExcelTestCar read : readList) {
					if (Objects.equals(read.getCode(), "200")) {
						if (Objects.equals(read.getBrandName(), "null") ||
								Objects.equals(read.getEngineNo(), "null") ||
								Objects.equals(read.getModelNo(), "null") ||
								Objects.equals(read.getInitialRegistrationDate(), "null") ||
								Objects.equals(read.getVin(), "null")) {
							read.setCode("404");
							read.setMsg("未匹配到相关数据");
							read.setBrandName(null);
							read.setEngineNo(null);
							read.setVin(null);
							read.setInitialRegistrationDate(null);
							read.setModelNo(null);
						}
					} else if (Objects.equals(read.getCode(), "500")) {
						JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(read));
						String plaintext = read.getPlateNo();
						JSONObject post = ApiUtils.queryApi("http://api.zjbhsk.com/bg/carInfo", jsonObject, plaintext);
						read.setCode(post.getString("code"));
						read.setMsg(post.getString("msg"));
						try {
							JSONObject data = post.getJSONObject("data");
							if (Objects.nonNull(data)) {
								read.setEngineNo(data.getString("engineNo"));
								read.setBrandName(data.getString("brandName"));
								read.setVin(data.getString("vin"));
								read.setInitialRegistrationDate(data.getString("initialRegistrationDate"));
								read.setModelNo(data.getString("modelNo"));
							}
						} catch (Exception e) {
							read.setEngineNo(post.getString("data"));
							log.error(e.getMessage());
						}
					}
				}
				excelTestCars.addAll(readList);
			}
			setExcelRespProp(response, DateUtils.dateTimeNow() + "车五项最终结果");
			EasyExcel.write(response.getOutputStream())
					.head(ExcelTestCar.class)
					.excelType(ExcelTypeEnum.XLSX)
					.sheet("车五项结果")
					.doWrite(excelTestCars);
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}


	@PostMapping("/testParse")
	public void testParse(MultipartFile excel, int sheetNo, HttpServletResponse response) {
		try (OutputStream out = response.getOutputStream()) {
			// 设置Excel响应属性并写出处理后的结果到Excel
			setExcelRespProp(response, DateUtils.dateTimeNow() + "1120车五项待测试");
			ExcelWriter excelWriter = EasyExcel.write(out).build();
			List<ExcelTestCar> readList = EasyExcel.read(excel.getInputStream())
					.headRowNumber(1)
					.head(ExcelTestCar.class)
					.sheet(sheetNo - 1)
					.doReadSync();
			// 按照每2000条数据划分任务
			int totalSize = readList.size();
			int batchSize = 20000;
			for (int i = 0; i < totalSize; i += batchSize) {
				int endIndex = Math.min(i + batchSize, totalSize);
				List<ExcelTestCar> subList = readList.subList(i, endIndex);
				WriteSheet writeSheet = EasyExcel.writerSheet(i + 1).head(ExcelTestCar.class).build();
				excelWriter.write(subList, writeSheet);
			}
			excelWriter.finish();
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}

	@PostMapping("/testCarMany")
	public void testCarMany(MultipartFile excel, int sheetNo, HttpServletResponse response) {
		try {
			// 读取Excel数据
			List<ExcelTestCar> readList = EasyExcel.read(excel.getInputStream())
					.headRowNumber(1)
					.head(ExcelTestCar.class)
					.sheet(sheetNo - 1)
					.doReadSync();

			// 创建线程池，根据实际情况可调整线程数量
			ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
			List<Future<List<ExcelTestCar>>> futures = new ArrayList<>();

			// 按照每2000条数据划分任务
			int totalSize = readList.size();
			int batchSize = 2000;
			for (int i = 0; i < totalSize; i += batchSize) {
				int endIndex = Math.min(i + batchSize, totalSize);
				List<ExcelTestCar> subList = readList.subList(i, endIndex);

				// 提交任务到线程池
				Future<List<ExcelTestCar>> future = executorService.submit(new ExcelTestCarTask(subList));
				futures.add(future);
			}

			// 收集处理后的结果
			List<ExcelTestCar> processedList = new ArrayList<>();
			for (Future<List<ExcelTestCar>> future : futures) {
				try {
					processedList.addAll(future.get());
				} catch (Exception e) {
					log.error("获取任务结果时出错: " + e.getMessage());
				}
			}

			// 关闭线程池
			executorService.shutdown();

			log.info(processedList.size() + "条样例测试完毕！");

			// 设置Excel响应属性并写出处理后的结果到Excel
			setExcelRespProp(response, DateUtils.dateTimeNow() + "车五项测试结果");
			EasyExcel.write(response.getOutputStream())
					.head(ExcelTestCar.class)
					.excelType(ExcelTypeEnum.XLSX)
					.sheet("车五项测试结果" + sheetNo)
					.doWrite(processedList);
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}

	// 内部类，用于定义每个任务的具体操作
	private static class ExcelTestCarTask implements Callable<List<ExcelTestCar>> {

		private List<ExcelTestCar> subList;

		public ExcelTestCarTask(List<ExcelTestCar> subList) {
			this.subList = subList;
		}

		@Override
		public List<ExcelTestCar> call() {
			for (ExcelTestCar read : subList) {
				JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(read));
				String plaintext = read.getPlateNo();
				JSONObject post = ApiUtils.queryApi("http://api.zjbhsk.com/bg/carInfo", jsonObject, plaintext);
				read.setCode(post.getString("code"));
				read.setMsg(post.getString("msg"));
				try {
					JSONObject data = post.getJSONObject("data");
					if (Objects.nonNull(data)) {
						read.setEngineNo(data.getString("engineNo"));
						read.setBrandName(data.getString("brandName"));
						read.setVin(data.getString("vin"));
						read.setInitialRegistrationDate(data.getString("initialRegistrationDate"));
						read.setModelNo(data.getString("modelNo"));
					}
				} catch (Exception e) {
					read.setEngineNo(post.getString("data"));
					log.error(e.getMessage());
				}
			}
			return subList;
		}
	}

	public static void setExcelRespProp(HttpServletResponse response, String rawFileName) throws UnsupportedEncodingException {
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setCharacterEncoding("utf-8");
		String fileName = URLEncoder.encode(rawFileName, "UTF-8").replaceAll("\\+", "%20");
		response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
	}


}
