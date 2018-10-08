package simplex;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;
import javax.swing.JTextArea;
public class SimplexMethod{
    private DefaultTableModel model;
    private DefaultTableModel modelHasil;
    private JTable jTableTemp;
    private JTable jTable_SimplexHasil;
    private JTextArea jTextArea_Kesimpulan;
    private int Iterasi;
    SimplexMethod(){
    Iterasi=1;
    }
    public void setJTable(JTable jTableTemp){
        this.jTableTemp=jTableTemp;     
    }
    public void setTabelHasil(JTable jTable_SimplexHasil){
        this.jTable_SimplexHasil=jTable_SimplexHasil;
    }
    public void SetModel(){
        model=(DefaultTableModel) jTableTemp.getModel();
        modelHasil=(DefaultTableModel) jTable_SimplexHasil.getModel();
    }
    public void setTextArea(JTextArea jTextArea_Kesimpulan){
        this.jTextArea_Kesimpulan=jTextArea_Kesimpulan;
    }
        String[] dTrainingFinal={"Z","x1","x2","x3"}; 
	String[] var={"Z","S1","S2","S3"};
	double[][] nilai={{-150,-400,-600,0,0,0,0,0}, 
					{4,3,1,1,0,0,134,0},	 	 
					{2,4,2,0,1,0,174,0},		
					{6,9,4,0,0,1,54,0}};		
	int kolomEV(){ //mencari nilai terkecil dari baris Z
		int idx=0;
                double temp=(double)model.getValueAt(0,0);
		for (int i=0;i<model.getColumnCount()-2 ;i++ ) {
                    double nilaiTerkecil=(double)model.getValueAt(0,i);
			if(temp > nilaiTerkecil){
                                temp=nilaiTerkecil;
				idx=i;
			}	
		}
		return idx;
	}
	void hitungRat(){//menghitung rasio
		for(int i=1;i<model.getRowCount();i++){
                    double nilaiNK=(double)model.getValueAt(i,(model.getColumnCount()-2));
                    double nilaiEV=(double)model.getValueAt(i, kolomEV());
                        double tmp=nilaiNK/nilaiEV;
                        model.setValueAt(tmp, i, (model.getColumnCount()-1));
		}
	}
	int barisLV(){ //mencari nilai rasio terkecil
		int idx=1;
                double nilaiKecil=(double)model.getValueAt(1, (model.getColumnCount()-1));
		for (int i=1; i< model.getRowCount() ;i++ ) {
                    double tmp= (double) model.getValueAt(i, (model.getColumnCount()-1));
			if(nilaiKecil >tmp){
				nilaiKecil=tmp;
				idx=i;
			}
		}
		return idx; 
	}
	boolean statusOptimal(){ //mengetahui apakah z bernilai optimal atau tydack
		boolean stat=false;
                double nilaiKecil;
                nilaiKecil =(double) model.getValueAt(0, 0);
		for (int i=0;i<model.getColumnCount()-1 ;i++ ) {//
                    double temp=(double)model.getValueAt(0, i);
		 	if(nilaiKecil> temp){
                            nilaiKecil=temp;
		 	}
		 }
			if(nilaiKecil>=0){
			stat=true;
		} 
		return stat;
	}
	void hitungBarisBaru(){ //menghitung baris kunci baru (BV)
                double lokasiLvEv=(double)model.getValueAt(barisLV(), kolomEV());
		for (int j=0;j<(model.getColumnCount()-1);j++ ) {
                        double kolomLV=((double)model.getValueAt(barisLV(), j));
			 double hasil=kolomLV/lokasiLvEv;
                         model.setValueAt(hasil, barisLV(),j);  
		}
                
	}
	void hitungBarisLain(){ // menghitung nilai tabel selain baris BV
            double nilaiX;
		for (int i=model.getRowCount()-1;i>=0 ;i--) { //mod
                    double barisLamaEV=(double) model.getValueAt(i, kolomEV());
                    double nilaiNK=(double) model.getValueAt(i, (model.getColumnCount()-2));
                    double nilaiNkLV=(double) model.getValueAt(barisLV(), (model.getColumnCount()-2));
                    double lokasiLvEv=(double) model.getValueAt(barisLV(), kolomEV());
			if((barisLamaEV != 0.0) &&  (nilaiNK!= nilaiNkLV)){
                                nilaiX=barisLamaEV/lokasiLvEv;
				for (int j=0;j<model.getColumnCount()-1 ;j++ ) {
                                                double barisLama=(double)model.getValueAt(i,j);
                                                double barisLV=(double)model.getValueAt(barisLV(),j); 
						 double hasil= barisLama-(nilaiX* barisLV);
                                                 model.setValueAt(hasil, i, j);
				}
			}
		}         
	}
	void ubahVarBV(){ 
	 var[ barisLV()]=dTrainingFinal[ barisLV()];
	}
	void kesimpulan(){
		for (int i=0;i<model.getRowCount();i++ ) {
                        jTextArea_Kesimpulan.append(var[i]+": "+model.getValueAt(i, (model.getColumnCount()-2))+"\n");
		}		
                strVarCmp();
	}
        void strVarCmp(){
            List<String> str=new ArrayList<>();
            for (int i = 0; i < var.length; i++) {
                if(var[i].equals(dTrainingFinal[i])){
                }else{
                    str.add(dTrainingFinal[i]);
                }
            }
        for (String str1 : str) {
            jTextArea_Kesimpulan.append(str1 + " : 0.0\n");
        }
        }
        void getAll(){
            double tmp;
            String it=""+Iterasi;
             Vector<Double> ls=new Vector<>(model.getColumnCount());
            for (int i = 0; i < model.getRowCount(); i++) {
                ls.clear();
                for (int j = 0; j < model.getColumnCount(); j++) {
                    tmp=(double)model.getValueAt(i, j);
                    ls.add(tmp);
                }
                if(i!=0){
                    it=" ";
                }
                modelHasil.addRow(new Object[]{it,var[i],ls.get(0),ls.get(1),ls.get(2),ls.get(3),ls.get(4),ls.get(5),ls.get(6),ls.get(7)});
            }
        }
        	void calc(){ // menghitung tabel hingga nilai Z optimal 
		while( statusOptimal()==false){	 
                
                hitungRat();
                hitungBarisBaru();
                ubahVarBV();
                hitungBarisLain();
                getAll();
                Iterasi++;
		}
                kesimpulan();
	}
}
