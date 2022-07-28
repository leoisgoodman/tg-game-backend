/**
 * tg学习代码
 */
package com.tggame.bet.vo;

import com.tggame.bet.entity.BetOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 投注 VO
 *
 * @author tg
 */
@Data
public class BetOrderSaveVO extends BetOrder implements java.io.Serializable {

    @ApiModelProperty(value = "号码List")
    private List<String> numList;
}
