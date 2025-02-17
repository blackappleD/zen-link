package com.mkc.api.dto.ck.res;

import lombok.Data;

import java.io.Serializable;

/**
 *人企信息核验认证响应
 * @author tqlei
 * @date 2023/10/12 10:33
 */

@Data
public class EntPersonRespDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 人企关系是否匹配（0不匹配，1 匹配）
     */
    private Integer peMatch;




    /**
     * 企业是否匹配（0不匹配，1匹配）
     */
    private Integer entMatch;


    private EntDetail entMatchDetail;

    private PeMatchDetail peMatchDetail;


    @Data
    public static class EntDetail{
        /**
         * 统一信用代码是否匹配（0不匹配，1匹配）
         */
        private Integer entNoM;

        /**
         * 企业名称是否匹配（0 配，1匹配） 0不匹
         */
        private Integer entNameM;


        public  EntDetail(Integer entNameM, Integer entNoM) {
            this.entNoM = entNoM;
            this.entNameM = entNameM;
        }
    }


    @Data
    public static class PeMatchDetail{
        /**
         * 姓名是否匹配（0不匹配，1匹配）
         */
        private Integer certNameM;

        /**
         * 证件是否匹配（-1库无,1匹配,0不匹配）
         */
        private Integer certNoM;


        public PeMatchDetail(Integer certNameM, Integer certNoM) {
            this.certNameM = certNameM;
            this.certNoM = certNoM;
        }
    }


}
