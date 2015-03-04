package com.bianlz.ndg.p8.protoBuf;

public class ProtoTester {
	private static byte[] encode(SubscribeReqProto.SubscribeReq req){
		return req.toByteArray();
	}
	private static SubscribeReqProto.SubscribeReq decode(byte[] body)throws Exception{
		return SubscribeReqProto.SubscribeReq.parseFrom(body);
	}
	private static SubscribeReqProto.SubscribeReq createReq(){
		SubscribeReqProto.SubscribeReq.Builder builder = SubscribeReqProto.SubscribeReq.newBuilder();
		builder.setSubReqID(123);
		builder.setUserName("tester");
		builder.setProductName("book");
		builder.setPhoneNumber("135XXXXXXX");
		builder.setAddress("earth");
		return builder.build();
	}
	public static void main(String[] args)throws Exception {
		// TODO Auto-generated method stub
		SubscribeReqProto.SubscribeReq req = createReq();
		System.err.println(req.toString());
		SubscribeReqProto.SubscribeReq req2 = decode(encode(req));
		System.out.println(req2);
	}

}
