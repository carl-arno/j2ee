package pers.hawk.room.dbserver;

import java.util.List;

import pers.hawk.room.z.xml.entity.Root3;

/**
 * 能耗数据（业务层）
 */
public class EnergyService extends JDBCMain {

	public long add(Root3 root3) {

		StringBuffer stringBuffer = new StringBuffer();
		// stringBuffer.append("");

		for (int i = 0; i < root3.getData().getMeters().getMeter().size(); i++) {
			stringBuffer.append(" insert into MeterEnergyInit(meterCode,collectDate,curValue,BranchID) ");
			stringBuffer.append(" values('");
			stringBuffer.append(root3.getData().getMeters().getMeter().get(i).getId());
			stringBuffer.append("','");
			stringBuffer.append(root3.getData().getTime());
			stringBuffer.append("',");
			stringBuffer.append(root3.getData().getMeters().getMeter().get(i).getValue());
			stringBuffer.append(",");
			stringBuffer.append(root3.getBranchId());
			stringBuffer.append(");");
		}

		return add(stringBuffer.toString());
	}

	/**
	 * @param metercode
	 * @return
	 */
	public List<String[]> getMeterByCode(String metercode){
		return  select("select top 1 id,metercode,metername,branchid from meter where metercode='"+metercode+"' ;");
	}

}
