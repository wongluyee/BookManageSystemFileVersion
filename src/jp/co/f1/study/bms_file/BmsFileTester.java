package jp.co.f1.study.bms_file;

public class BmsFileTester {

	public static void main(String[] args) {
		try {
			while (true) {
				BmsFunctionFile bms = new BmsFunctionFile();
				bms.displayMenu();

				if (bms.selectFunctionFromMenu() == 9) {
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e + "という例外が発生しました。");
		}

	}
}
