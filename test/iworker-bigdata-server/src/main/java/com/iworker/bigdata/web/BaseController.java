/**
 * 
 */
package com.iworker.bigdata.web;

import com.iworker.api.common.ApiReturnResult;

/**
 * @author wcj
 *
 */
public class BaseController {

	protected ApiReturnResult ret;

	public BaseController() {
		this.ret = new ApiReturnResult();
		this.ret.setRet(0);
	}
}
