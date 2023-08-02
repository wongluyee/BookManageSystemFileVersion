package jp.co.f1.study.bms_file;

import java.util.ArrayList;

import jp.co.f1.study.common.FileIn;
import jp.co.f1.study.common.FileOut;
import jp.co.f1.study.common.KeyIn;
import jp.co.f1.study.common.MyFormat;

public class BmsFunctionFile {
	MyFormat fm = new MyFormat();
	private KeyIn objKeyIn = new KeyIn();
	private final String TAB = "\t";

	// データ分割用定数
	private final static String STR_COMMA = ",";

	// 書籍データファイルのパス
	private final static String FILENAME = "file\\bmsFileDB.csv";
	private final static String REPRESENT_FILENAME = "file\\bmsIniFile.csv";

	// 書籍データを格納するArrayListオブジェクト
	private ArrayList<String> isbnArrayList = new ArrayList<String>();
	private ArrayList<String> titleArrayList = new ArrayList<String>();
	private ArrayList<Integer> priceArrayList = new ArrayList<Integer>();

	// 提供ライブラリのオブジェクト
	private FileIn objFileIn = new FileIn();
	private FileOut objOut = new FileOut();

	// メニュー表示機能
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

	// 選択機能
	public int selectFunctionFromMenu() {
		int num = objKeyIn.readInt();

		switch (num) {
		case 1:
			addFunction();
			break;
		case 2:
			deleteFunction();
			break;
		case 3:
			updateFunction();
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

	public void loadIntoMemoryFromFileInitially() {
		String strLine;
		String[] bookArray = new String[3];
		// FileInクラスを利用して初期データファイルをオープンする。
		if (objFileIn.open(REPRESENT_FILENAME) == false) {
			System.out.println("初期化ファイルのオープンに失敗しました。処理を中断します。");
			System.exit(1);
		}

		// FileInクラスを利用して初期データファイルから書籍データを読み込む。
		while ((strLine = objFileIn.readLine()) != null) {

			// splitメソッドを利用して、読み込んだ書籍データを,(カンマ)で分割した配列データを受け取る。
			bookArray = strLine.split(STR_COMMA);

			// 読み込んだ1行データのISBNやTITLE、またはPRICEのデータが1つでも不足している場合、
			// エラーメッセージを表示し、プログラムを停止
			if (bookArray.length != 3) {
				System.out.println("不正なデータが存在します。データを修正した後で再度実行してください。");
				System.exit(2);
			}

			// 分割した配列データがヘッダーデータだった場合には処理をスキップし、ヘッダーデータではない場合、各ArrayListオブジェクトに格納する。
			try {
				if (!bookArray[0].equals("isbn")) {
					isbnArrayList.add(bookArray[0]);
					titleArrayList.add(bookArray[1]);
					priceArrayList.add(Integer.parseInt(bookArray[2]));
				}
			} catch (NumberFormatException e) {
				// priceがintegerではない場合、エラーメッセージ表示
				System.out.println("価格に文字が含まれています。データを修正した後で再度実行してください。");
				System.exit(3);
			}

		}
		// FileInクラスを利用して書籍データファイルをクローズする。
		// こちらのコードを加えるとIllegalStateException: Scanner closedが発生します。
//		if (objFileIn.close() == false) {
//			System.out.println("初期化ファイルのクローズに失敗しました。処理を中断します。");
//			System.exit(4);
//		}

		// writeIntoFileFromMemoryメソッドを呼び出し、読み込んだ初期データ全てを書籍データファイルに書き込む。
		writeIntoFileFromMemory();
	}

	// データ読み込み
	public void loadIntoMemoryFromFile() {
		String strLine;
		String[] bookArray = new String[3];

		// 書籍データを格納する各ArrayListオブジェクト内のデータを初期化する
		isbnArrayList.clear();
		titleArrayList.clear();
		priceArrayList.clear();

		// データファイルをオープンする。失敗する場合、メッセージ表示&プログラム終了
		if (objFileIn.open(FILENAME) == false) {
			System.out.println("ファイルオープンに失敗しました。処理を中断します。");
			System.exit(5);
		}

		// 読み込み可能なデータがなくなるまで処理を繰り返す
		while ((strLine = objFileIn.readLine()) != null) {
			// splitメソッドを利用して、読み込んだ書籍データを,(カンマ)で分割した配列データを受け取る
			bookArray = strLine.split(STR_COMMA);

			// 読み込んだ1行データのISBNやTITLE、またはPRICEのデータが1つでも不足している場合、
			// エラーメッセージを表示し、プログラムを停止
			if (bookArray.length != 3) {
				System.out.println("不正なデータが存在します。データを修正した後で再度実行してください。");
				System.exit(6);
			}

			// 分割した配列データがヘッダーだった場合には処理をスキップし、ヘッダーではない場合には各ArrayListオブジェクトに格納する
			try {
				if (!bookArray[0].equals("isbn")) {
					isbnArrayList.add(bookArray[0]);
					titleArrayList.add(bookArray[1]);
					priceArrayList.add(Integer.parseInt(bookArray[2]));
				}
			} catch (NumberFormatException e) {
				// priceがintegerではない場合、エラーメッセージ表示
				System.out.println("価格に文字が含まれています。データを修正した後で再度実行してください。");
				System.exit(7);
			}
		}
		// loadIntoMemoryFromFileメソッド内で読み込んだデータが1件も存在しない時
		// 「loadIntoMemoryFromFileInitiallyメソッド」を呼び出すように修正し、初期データ登録機能を実装します。
		if (isbnArrayList.size() == 0) {
			System.out.println("読み込んだ書籍データが0件です。");
			System.out.println("初期データファイルからデータの読み込みを行いました。");
			loadIntoMemoryFromFileInitially();
		}
		// FileInクラスを利用して書籍データファイルをクローズする
		if (objFileIn.close() == false) {
			System.out.println("ファイルクローズに失敗しました。処理を中断します。");
			System.exit(8);
		}
	}

	// bookList表示
	public void bookListDisplay() {
		System.out.println("***書籍一覧***");
		System.out.println("No." + TAB + "ISBN" + TAB + "Title" + TAB + "Price");
		System.out.println("----------------------------------");
		for (int i = 0; i < isbnArrayList.size(); i++) {
			String formattedMoney = fm.moneyFormat(priceArrayList.get(i));
			System.out.println(
					(i + 1) + "." + TAB + isbnArrayList.get(i) + TAB + titleArrayList.get(i) + TAB + formattedMoney);
		}
		System.out.println("----------------------------------");
	}

	// 一覧機能
	public void listFunction() {
		loadIntoMemoryFromFile();
		bookListDisplay();
	}

	// Writeデータ
	public void writeIntoFileFromMemory() {
		// FileOutクラスを利用して書籍データファイルをオープンする
		if (objOut.open(FILENAME) == false) {
			System.out.println("書き込みファイルのオープンに失敗しました。処理を中断します。");
			System.exit(9);
		}

		// Header書く
		objOut.writeln("isbn,title,price");

		// Data書く
		for (int i = 0; i < isbnArrayList.size(); i++) {
			objOut.writeln(
					isbnArrayList.get(i) + STR_COMMA + titleArrayList.get(i) + STR_COMMA + priceArrayList.get(i));
		}

		if (objOut.close() == false) {
			System.out.println("書き込みファイルのクローズに失敗しました。処理を中断します。");
			System.exit(10);
		}
	}

	// 登録機能
	public void addFunction() {
		loadIntoMemoryFromFile();

		String inputIsbn;
		String inputTitle;
		String inputPrice;
		// Integerに変換用
		int intInputPrice;

		loopIsbn: while (true) {
			System.out.println("***書籍情報登録***");
			// ISBN入力
			System.out.println("ISBNを入力してください。");
			System.out.println("【ISBN】⇒");
			inputIsbn = objKeyIn.readKey();

			// 空文字かどうかチェック
			if (inputIsbn.equals("")) {
				System.out.println("空文字が入力されました。ISBNを入力して下さい！");
				continue;
			}

			// ISBN重複しているかどうかチェック
			for (int i = 0; i < isbnArrayList.size(); i++) {
				if (inputIsbn.equals(isbnArrayList.get(i))) {
					System.out.println("入力ISBNは既に登録されています。:" + inputIsbn);
					continue loopIsbn;
				}
			}

			isbnArrayList.add(inputIsbn);
			break;
		}

		while (true) {
			// Title入力
			System.out.println("タイトルを入力してください。");
			System.out.println("【タイトル】⇒");
			inputTitle = objKeyIn.readKey();

			// 空文字かどうかチェック
			if (inputTitle.equals("")) {
				System.out.println("空文字が入力されました。タイトルを入力して下さい！");
				continue;
			}
			titleArrayList.add(inputTitle);
			break;
		}

		while (true) {
			// Price入力
			System.out.println("価格を入力してください。");
			System.out.println("【価格】⇒");
			inputPrice = objKeyIn.readKey();

			// 空文字かどうかチェック
			if (inputPrice.equals("")) {
				System.out.println("空文字が入力されました。価格を入力して下さい！");
				continue;
			}

			// 入力されたものは数字かどうかチェック
			try {
				intInputPrice = Integer.parseInt(inputPrice);
			} catch (NumberFormatException e) {
				System.out.println("文字が入っています。数字だけを入力してください。");
				continue;
			}

			// 入力された数字は０以上かどうかチェック
			if (intInputPrice <= 0) {
				System.out.println("0 以上の価格を入力してください。");
				continue;
			}

			priceArrayList.add(intInputPrice);
			break;
		}

		writeIntoFileFromMemory();

		String formattedMoney = fm.moneyFormat(intInputPrice);

		System.out.println("");
		System.out.println("***登録済書籍情報***");
		System.out.println("ISBN" + TAB + "Title" + TAB + "Price");
		System.out.println("----------------------------------");
		System.out.println(inputIsbn + TAB + inputTitle + TAB + formattedMoney);
		System.out.println("----------------------------------");
		System.out.println("上記書籍が登録されました。");
	}

	// 削除機能
	public void deleteFunction() {
		// 最新の書籍データを読み込む
		loadIntoMemoryFromFile();
		bookListDisplay();
		int index;

		while (true) {
			System.out.println("***削除対象の書籍選択***");
			System.out.println("削除したい書籍（ISBN）を選択してください⇒");
			String selectedIsbn = objKeyIn.readKey();

			// 入力したISBNでIndexを探す
			index = isbnArrayList.indexOf(selectedIsbn);

			// 入力したISBNが存在しない場合、エラーメッセージ表示
			if (index == -1) {
				System.out.println("入力ISBN：" + selectedIsbn + " は存在しませんでした。正しいISBNを入力してください。");
				continue;
			}

			break;
		}
		String formattedMoney = fm.moneyFormat(priceArrayList.get(index));
		System.out.println("***削除対象書籍情報***");
		System.out.println("isbn" + TAB + "title" + TAB + "price");
		System.out.println("----------------------------------");
		System.out.println(isbnArrayList.get(index) + TAB + titleArrayList.get(index) + TAB + formattedMoney);
		System.out.println("----------------------------------");
		System.out.println("上記書籍を削除しますか＜y/n＞");
		String confirm = objKeyIn.readKey();

		if (confirm.toLowerCase().equals("y")) {
			System.out.println("ISBN:" + isbnArrayList.get(index) + "の書籍が削除されました。");
			System.out.println("");
			isbnArrayList.remove(index);
			titleArrayList.remove(index);
			priceArrayList.remove(index);
			writeIntoFileFromMemory();
		} else {
			System.out.println("削除を中止し、メニュー画面に戻ります。");
		}
	}

	// 更新機能
	public void updateFunction() {
		// 最新の書籍データを読み込む
		loadIntoMemoryFromFile();
		bookListDisplay();

		int index;
		String newTitle;
		int newPrice;

		while (true) {
			System.out.println("***変更対象の書籍選択***");
			System.out.println("変更したい書籍（ISBN）を選択してください⇒");

			String book = objKeyIn.readKey();
			// Find book
			index = isbnArrayList.indexOf(book);

			// 入力したISBNが存在しない場合、エラーメッセージ表示
			if (index == -1) {
				System.out.println("入力ISBN：" + book + " は存在しませんでした。正しいISBNを入力してください。");
				continue;
			}

			break;
		}

		String oldTitle = titleArrayList.get(index);
		int oldPrice = priceArrayList.get(index);

		// 新しい情報の入力
		while (true) {
			System.out.println("タイトル【" + oldTitle + "】変更⇒");
			newTitle = objKeyIn.readKey();

			if (newTitle.equals("")) {
				System.out.println("空文字が入力されました。タイトルを入力して下さい！");
				continue;
			}

			break;
		}

		String formattedOldPrice = fm.moneyFormat(oldPrice);
		while (true) {
			System.out.println("価格【" + formattedOldPrice + "】変更⇒");
			newPrice = objKeyIn.readInt();

			// 入力された数字は０以上かどうかチェック
			if (newPrice <= 0) {
				System.out.println("0 以上の価格を入力してください。");
				continue;
			}
			break;
		}

		// 情報を更新する
		titleArrayList.set(index, newTitle);
		priceArrayList.set(index, newPrice);
		writeIntoFileFromMemory();

		String formattedNewPrice = fm.moneyFormat(newPrice);
		System.out.println("***更新済書籍情報***");
		System.out.println("下記のように書籍情報が更新されました。");
		System.out.println("----------------------------------");
		System.out.println(TAB + "変更前" + TAB + "変更後");
		System.out.println("ISBN" + TAB + isbnArrayList.get(index) + "→" + isbnArrayList.get(index));
		System.out.println("Title" + TAB + oldTitle + "→" + newTitle);
		System.out.println("Price" + TAB + formattedOldPrice + "→" + formattedNewPrice);
		System.out.println("----------------------------------");
	}
}
