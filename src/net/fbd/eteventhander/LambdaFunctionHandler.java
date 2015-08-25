package net.fbd.eteventhander;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.amazonaws.services.lambda.runtime.events.SNSEvent.SNSRecord;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.util.json.JSONObject;

public class LambdaFunctionHandler implements RequestHandler<SNSEvent, String> 
{
	// Private Variables
	private static final String S3_BUCKET_NAME = "snmvideosinput";

    @Override
    public String handleRequest(SNSEvent input, Context context) 
    {
    	try 
		{
			// Logger
			LambdaLogger logger = context.getLogger();
			
			// Get Event Record
			SNSRecord record = input.getRecords().get(0);
			
			// Get Input File Name
			JSONObject rootObject = new JSONObject(record.getSNS().getMessage());
			JSONObject inputObject = (JSONObject) rootObject.get("input");
			String fileName = inputObject.getString("key");
			
			// S3 Client
			AmazonS3 s3Client = new AmazonS3Client();
			
			// Delete File
			s3Client.deleteObject(S3_BUCKET_NAME, fileName);
			
			//Log
			logger.log(fileName + " was deleted successfully."); 
		}
    	catch (Exception e)
		{
			throw new RuntimeException(e);
		}

		return "OK";
    }

}
