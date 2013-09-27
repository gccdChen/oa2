package scau.duolian.oa.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import scau.duolian.oa.util.JsonUtil;
import scau.duolian.oa.util.StringUtil;
import android.util.Log;

public class BaseMessage {

	private int code;
	private String message;
	private String resultSrc;
	private Map<String, Object> resultMap;
	private Map<String, ArrayList<? extends Object>> resultList;

	public BaseMessage() {
		this.resultMap = new HashMap<String, Object>();
		this.resultList = new HashMap<String, ArrayList<? extends Object>>();
	}

	@Override
	public String toString() {
		return code + " | " + message + " | " + resultSrc;
	}

	public boolean isSuccess() {
		return this.code == C.response.success;
	}

	public int getCode() {
		return this.code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getResult() {
		return this.resultSrc;
	}

	public Object getResult(String modelName) throws EntityEmptyException {
		setResult();
		Object model = this.resultMap.get(modelName);
		// catch null exception
		if (model == null) {
			Log.i("getResult", "null");
			throw new EntityEmptyException("Message data is empty");
		}
		return model;
	}

	public ArrayList<? extends Object> getResultList(String modelName)  {
		setResult();
		modelName = getModelName(modelName);
		ArrayList<? extends Object> modelList = this.resultList.get(modelName);
		// catch null exception
		if (modelList == null || modelList.size() == 0) {
//			throw new EntityEmptyException("Message data list is empty");
		}
		return modelList;
	}

	private final static List<String> singleStrings = new ArrayList<String>();
	private final static List<String> singleInt = new ArrayList<String>();
	static {
		singleInt.add("isadmin");
		singleInt.add("newmessage");
	}
	
	private final static List<String> ignoreCols = new ArrayList<String>();
	
	public void setResult(String result){
		this.resultSrc = result;
		resultMap.clear();
		resultList.clear();
	}
	
	/**
	 * 
	 * @param result
	 *            json like { "user":[{"acount":"admin"},{"acount":"user"}]
	 *            ,"msg":[{"content":"the card is finded!"},{"content",
	 *            "the server will update in 12:00"}] }
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void setResult() {
		String result = this.resultSrc;
		if (result.length() > 0 && resultMap.isEmpty() && resultList.isEmpty()) {
			JSONObject jsonObject = null;
			String jsonKey = null;
			try {
				jsonObject = new JSONObject(result);
				Iterator<String> it = jsonObject.keys();
				
				while (it.hasNext()) {
					jsonKey = it.next();
					String modelName = getModelName(jsonKey);
					//
					if (singleInt.contains(jsonKey)) {
						this.resultMap.put(jsonKey, jsonObject.getInt(jsonKey));
					} else if (singleStrings.contains(jsonKey)) {
						this.resultMap.put(jsonKey,jsonObject.getString(jsonKey));
					} else{
						try{
							toModel(jsonObject, jsonKey, modelName);
						}catch(Exception e){
							Log.w(""+e.toString(), jsonKey);
							continue;
						}
					}
				}
			} catch (JSONException e) {
				Log.w("fail field:", jsonKey);
				e.printStackTrace();
			} 
		}
	}

	private void toModel(JSONObject jsonObject, String jsonKey, String modelName) throws Exception {
		// TODO Auto-generated method stub
		String modelClassName = C.model.packet + modelName;
		JSONArray modelJsonArray = jsonObject.optJSONArray(jsonKey);

		if (modelJsonArray == null) {
			JSONObject modelJsonObject = jsonObject.optJSONObject(jsonKey);
			if (modelJsonObject == null) {
				throw new Exception("Message result is invalid");
			}
			this.resultMap.put(modelName,JsonUtil.json2model(modelClassName, modelJsonObject));
		} else {
			ArrayList<BaseModel> modelList = new ArrayList<BaseModel>();
			for (int i = 0; i < modelJsonArray.length(); i++) {
				JSONObject modelJsonObject = modelJsonArray.optJSONObject(i);
				modelList.add(JsonUtil.json2model(modelClassName,modelJsonObject));
			}
			this.resultList.put(modelName, modelList);
		}
	}

	private String getModelName(String str) {
		String[] strArr = str.split("\\W");
		if (strArr.length > 0) {
			str = strArr[0];
		}
		return StringUtil.ucfirst(str);
	}

	public class EntityEmptyException extends Exception {
		public EntityEmptyException(String string) {
			super(string);
		}
	}
}