package jp.co.f1.study.bms_file;

public class BmsFileTester {

	public static void main(String[] args) {
		while(true) {
			BmsFunctionFile bms = new BmsFunctionFile();
			bms.displayMenu();

			if (bms.selectFunctionFromMenu() == 9) {
				break;
			}
		}
	}
}
