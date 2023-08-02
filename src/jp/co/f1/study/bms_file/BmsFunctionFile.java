package jp.co.f1.study.bms_file;

import java.util.ArrayList;

import jp.co.f1.study.common.FileIn;
import jp.co.f1.study.common.FileOut;
import jp.co.f1.study.common.KeyIn;

public class BmsFunctionFile {
	private KeyIn objKeyIn = new KeyIn();
	private final String TAB = "\t";

	// データ分割用定数
	private final static String STR_COMMA = ",";

	// 書籍データファイルのパス
	private final static String FILENAME = "file\\bmsFileDB.csv";

	// 書籍データを格納するArrayListオブジェクト
	private ArrayList<String> isbnArrayList = new ArrayList<String>();
	private ArrayList<String> titleArrayList = new ArrayList<String>();
	private ArrayList<Integer> priceArrayList = new ArrayList<Integer>();

	// 提供ライブラリのオブジェクト
	private FileIn objFileIn = new FileIn();
	private FileOut objOut = new FileOut();

	public void displayMenu() {
		System.out.println("***書籍管理メニュー***");
		System.out.println("1. 登録");
		System.out.println("2. 削除");
		System.out.println("3. 変更");
		System.out.println("4. 一覧");
		System.out.println("9. 終了");
		System.out.println("-----------------------");
		System.out.println("番号を選択してください⇒");
	}

	public int selectFunctionFromMenu() {
		int num = objKeyIn.readInt();

		switch (num) {
		case 1:
			addFunction();
			break;
		case 2:
			System.out.println("selectFunctionFromMenuメソッドの中で「2. 削除」が選択されました。");
			break;
		case 3:
			System.out.println("selectFunctionFromMenuメソッドの中で「3. 変更」が選択されました。");
			break;
		case 4:
			listFunction();
			break;
		case 9:
			System.out.println("**処理を終了しました**");
			break;
		default:
			System.out.println("Menu番号の数値を入力してください。");
		}
		return num;
	}

	public void loadIntoMemoryFromFile() {
		String strLine;
		String[] bookArray = new String[3];

		// 書籍データを格納する各ArrayListオブジェクト内のデータを初期化する
		isbnArrayList.clear();
		titleArrayList.clear();
		priceArrayList.clear();

		// データファイルをオープンする
		objFileIn.open(FILENAME);

		// 読み込み可能なデータがなくなるまで処理を繰り返す
		while ((strLine = objFileIn.readLine()) != null) {
			// splitメソッドを利用して、読み込んだ書籍データを,(カンマ)で分割した配列データを受け取る
			bookArray = strLine.split(STR_COMMA);
			// 分割した配列データがヘッダーだった場合には処理をスキップし、ヘッダーではない場合には各ArrayListオブジェクトに格納する。
			if (!bookArray[0].equals("isbn")) {
				isbnArrayList.add(bookArray[0]);
				titleArrayList.add(bookArray[1]);
				priceArrayList.add(Integer.parseInt(bookArray[2]));
			}
		}
		// FileInクラスを利用して書籍データファイルをクローズする
		objFileIn.close();
	}

	public void bookListDisplay() {
		System.out.println("***書籍一覧***");
		System.out.println("No." + TAB + "ISBN" + TAB + "Title" + TAB + "Price");
		System.out.println("----------------------------------");
		for (int i = 0; i < isbnArrayList.size(); i++) {
			System.out.println((i + 1) + "." + TAB + isbnArrayList.get(i) + TAB + titleArrayList.get(i) + TAB
					+ priceArrayList.get(i));
		}
		System.out.println("----------------------------------");
	}

	public void listFunction() {
		loadIntoMemoryFromFile();
		bookListDisplay();
	}

	public void writeIntoFileFromMemory() {
		// FileOutクラスを利用して書籍データファイルをオープンする
		objOut.open(FILENAME);
		// Header書く
		objOut.writeln("isbn,title,price");
		for (int i = 0; i < isbnArrayList.size(); i++) {
			objOut.writeln(
					isbnArrayList.get(i) + STR_COMMA + titleArrayList.get(i) + STR_COMMA + priceArrayList.get(i));
		}
		objOut.close();
	}

	public void addFunction() {
		loadIntoMemoryFromFile();
		System.out.println("***書籍情報登録***");
		// ISBN入力
		System.out.println("ISBNを入力してください。");
		System.out.println("【ISBN】⇒");
		String inputIsbn = objKeyIn.readKey();
		isbnArrayList.add(inputIsbn);

		// Title入力
		System.out.println("タイトルを入力してください。");
		System.out.println("【タイトル】⇒");
		String inputTitle = objKeyIn.readKey();
		titleArrayList.add(inputTitle);

		// Price入力
		System.out.println("価格を入力してください。");
		System.out.println("【価格】⇒");
		String inputPrice = objKeyIn.readKey();
		priceArrayList.add(Integer.parseInt(inputPrice));

		writeIntoFileFromMemory();

		System.out.println("");
		System.out.println("***登録済書籍情報***");
		System.out.println("ISBN" + TAB + "Title" + TAB + "Price");
		System.out.println("----------------------------------");
		System.out.println(inputIsbn + TAB + inputTitle + TAB + inputPrice);
		System.out.println("----------------------------------");
		System.out.println("上記書籍が登録されました。");
	}
}
