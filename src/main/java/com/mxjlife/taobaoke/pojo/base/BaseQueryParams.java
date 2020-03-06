package com.mxjlife.taobaoke.pojo.base;

import lombok.Data;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * description:
 * author mxj
 * email mengxiangjie@hualala.com
 * date 2020/3/2 18:12
 */
@Data
public class BaseQueryParams {


        /** 数据状态 */
        private Integer dataStatus;
        /** 操作者 */
        private String operator;
        /** 备注 */
        private String remark;
        /** 数据时间开始 */
        private String startTime;
        /** 数据时间结束 */
        private String endTime;
        /** 当前页 */
        private Integer currentPage = 1;
        /** 页大小 */
        private Integer pageSize = 20;
        /** 开始的index, 用来分页查询, 计算产生 */
        private Integer startIndex;
        /** 排序字段 */
        private String orderBy;
        /** 排序方式 */
        private String orderType;
        /** 关键字 */
        private String keyWord;

        public void setOrderType(String orderType) {
                if(NumberUtils.isDigits(orderType)){
                        int v = Integer.parseInt(orderType);
                        if(v == 1){
                                this.orderType = "DESC";
                        }else if(v == 2){
                                this.orderType = "ASC";
                        }else {
                                this.orderType = "DESC";
                        }
                }else {
                        this.orderType = orderType;
                }

        }

        public Integer getStartIndex() {
                int i = (currentPage - 1) * pageSize;
                this.startIndex = i;
                return startIndex;
        }
        private void setStartIndex(Integer startIndex) {
        }
}
