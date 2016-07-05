package com.sap.sdc.hcp.bootcamp.odata;

import org.apache.olingo.odata2.api.ep.callback.TombstoneCallback;
import org.apache.olingo.odata2.api.ep.callback.TombstoneCallbackResult;

public class TombstomeCallbackImpl implements TombstoneCallback {

	@Override
	public TombstoneCallbackResult getTombstoneCallbackResult() {
		// TODO Auto-generated method stub
		System.out.println("Stop");
		TombstoneCallbackResult test = new TombstoneCallbackResult();
		test.setDeltaLink("12345");
		return test;
	}

}
