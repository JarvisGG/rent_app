package com.hc.xiaobairent.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Table;
import org.apache.poi.hwpf.usermodel.TableCell;
import org.apache.poi.hwpf.usermodel.TableIterator;
import org.apache.poi.hwpf.usermodel.TableRow;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.kymjs.kjframe.ui.BindView;

import com.ab.http.AbFileHttpResponseListener;
import com.ab.http.AbHttpUtil;
import com.hc.core.base.BaseActivity;
import com.hc.xiaobairent.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ZfMyRoomContract extends BaseActivity {

	// 初始化标题
	@BindView(id = R.id.menu_back, click = true)
	private ImageView menuBack;
	@BindView(id = R.id.menu_title)
	private TextView menuTitle;

	@BindView(id = R.id.word_contract)
	private WebView wordContract;

	private AbHttpUtil http;

	private Range range = null;
	private HWPFDocument hwpf = null;
	private String picturePath;
	private List pictures;
	private TableIterator tableIterator;
	private int presentPicture = 0;
	private int screenWidth;
	private FileOutputStream output;
	private File myFile;
	private String htmlPath;
	private String wordUrl;

	String html = null;

	@Override
	public void setRootView() {
		setAbContentView(R.layout.zf_activity_myroom_contract);
	}

	@Override
	public void initData() {
		super.initData();
		Bundle bundle = getIntent().getExtras();
		wordUrl = bundle.getString("wordUrl");

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;
		initTab();
		applyData();

		WebSettings settings = wordContract.getSettings();
		settings.setJavaScriptEnabled(true);
		wordContract.setInitialScale(100);
		settings.setSupportZoom(true);
		settings.setBuiltInZoomControls(true);
		settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //
		String uri = "file:///mnt/sdcard/xiaobaizu/doc/doc.html";
		wordContract.loadUrl(uri);
		System.out.println("htmlPath" + htmlPath);

	}

	private void applyData() {
		http = AbHttpUtil.getInstance(ZfMyRoomContract.this);

		http.get(wordUrl, new AbFileHttpResponseListener(wordUrl) {

			@Override
			public void onStart() {
				Log.d("downloadfile", "onStart");

			}

			@Override
			public void onFinish() {
			}

			@Override
			public void onProgress(long bytesWritten, long totalSize) {
				super.onProgress(bytesWritten, totalSize);
			}

			@Override
			public void onFailure(int statusCode, String content, Throwable error) {
				Toast.makeText(ZfMyRoomContract.this, "网络请求失败", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(int statusCode, File file) {
				// Toast.makeText(ZfMyRoomContract.this, "下载成功",
				// Toast.LENGTH_SHORT).show();
				getRange(file);
				makeFile();
				readAndWrite();
			}
		});
	}

	public void readAndWrite() {

		try {
			myFile = new File(htmlPath);
			output = new FileOutputStream(myFile);
			String utf = "<head><meta http-equiv='content-type'content='text/html;charset=utf-8'></head>";
			String head = "<html>" + utf + "<body>";
			String tagBegin = "<p>";
			String tagEnd = "</p>";
			output.write(head.getBytes());
			int numParagraphs = range.numParagraphs();
			for (int i = 0; i < numParagraphs; i++) {
				Paragraph p = range.getParagraph(i);

				if (p.isInTable()) {
					int temp = i;
					if (tableIterator.hasNext()) {
						String tableBegin = "<table style=\"border-collapse:collapse\" border=1bordercolor=\"black\">";
						String tableEnd = "</table>";
						String rowBegin = "<tr>";
						String rowEnd = "</tr>";
						String colBegin = "<td>";
						String colEnd = "</td>";

						Table table = tableIterator.next();

						html += tableBegin;
						output.write(tableBegin.getBytes());

						int rows = table.numRows();

						for (int r = 0; r < rows; r++) {
							html += rowBegin;
							output.write(rowBegin.getBytes());
							TableRow row = table.getRow(r);
							int cols = row.numCells();
							int rowNumParagraphs = row.numParagraphs();
							int colsNumParagraphs = 0;
							for (int c = 0; c < cols; c++) {
								html += colBegin;
								output.write(colBegin.getBytes());
								TableCell cell = row.getCell(c);
								int max = temp + cell.numParagraphs();
								colsNumParagraphs = colsNumParagraphs + cell.numParagraphs();
								for (int cp = temp; cp < max; cp++) {
									Paragraph p1 = range.getParagraph(cp);
									html += tagBegin;
									output.write(tagBegin.getBytes());
									writeParagraphContent(p1);
									html += tagEnd;
									output.write(tagEnd.getBytes());
									temp++;
								}
								html += colEnd;
								output.write(colEnd.getBytes());
							}
							int max1 = temp + rowNumParagraphs;
							for (int m = temp + colsNumParagraphs; m < max1; m++) {
								Paragraph p2 = range.getParagraph(m);
								temp++;
							}
							html += rowEnd;
							output.write(rowEnd.getBytes());
						}
						html += tableEnd;
						output.write(tableEnd.getBytes());
					}
					i = temp;
				}

				else {
					html += tagBegin;
					output.write(tagBegin.getBytes());
					writeParagraphContent(p);
					html += tagEnd;
					output.write(tagEnd.getBytes());
				}
			}
			String end = "</body></html>";
			html += end;
			output.write(end.getBytes());
			output.close();

			Log.e("html", html);
		} catch (Exception e) {
			System.out.println("readAndWrite Exception");
		}
	}

	public void makePictureFile() {
		String sdString = android.os.Environment.getExternalStorageState();

		if (sdString.equals(android.os.Environment.MEDIA_MOUNTED)) {
			try {
				File picFile = android.os.Environment.getExternalStorageDirectory();

				String picPath = picFile.getAbsolutePath() + File.separator + "xiaobaizu" + File.separator + "doc";

				File picDirFile = new File(picPath);

				if (!picDirFile.exists()) {
					picDirFile.mkdir();
				}
				File pictureFile = new File(picPath + File.separator + presentPicture + ".jpg");

				if (!pictureFile.exists()) {
					pictureFile.createNewFile();
				}

				picturePath = pictureFile.getAbsolutePath();

			} catch (Exception e) {
				System.out.println("PictureFile Catch Exception");
			}
		}
	}

	public void writePicture() {
		Picture picture = (Picture) pictures.get(presentPicture);

		byte[] pictureBytes = picture.getContent();

		Bitmap bitmap = BitmapFactory.decodeByteArray(pictureBytes, 0, pictureBytes.length);

		makePictureFile();
		presentPicture++;

		File myPicture = new File(picturePath);

		try {

			FileOutputStream outputPicture = new FileOutputStream(myPicture);

			outputPicture.write(pictureBytes);

			outputPicture.close();
		} catch (Exception e) {
			System.out.println("outputPicture Exception");
		}

		String imageString = "<img src=\"" + picturePath + "\"";

		if (bitmap.getWidth() > screenWidth) {
			imageString = imageString + " " + "width=\"" + screenWidth + "\"";
		}
		imageString = imageString + ">";

		try {
			output.write(imageString.getBytes());
		} catch (Exception e) {
			System.out.println("output Exception");
		}
	}

	public void writeParagraphContent(Paragraph paragraph) {
		Paragraph p = paragraph;
		int pnumCharacterRuns = p.numCharacterRuns();

		for (int j = 0; j < pnumCharacterRuns; j++) {

			CharacterRun run = p.getCharacterRun(j);

			if (run.getPicOffset() == 0 || run.getPicOffset() >= 1000) {
				if (presentPicture < pictures.size()) {
					writePicture();
				}
			} else {
				try {
					String text = run.text();
					if (text.length() >= 2 && pnumCharacterRuns < 2) {
						html += text;
						output.write(text.getBytes());
					} else {
						int size = run.getFontSize();
						int color = run.getColor();
						String fontSizeBegin = "<font size=\"" + decideSize(size) + "\">";
						String fontColorBegin = "<font color=\"" + decideColor(color) + "\">";
						String fontEnd = "</font>";
						String boldBegin = "<b>";
						String boldEnd = "</b>";
						String islaBegin = "<i>";
						String islaEnd = "</i>";
						html += fontSizeBegin;
						output.write(fontSizeBegin.getBytes());
						html += fontColorBegin;
						output.write(fontColorBegin.getBytes());

						if (run.isBold()) {
							html += boldBegin;
							output.write(boldBegin.getBytes());
						}
						if (run.isItalic()) {
							html += islaBegin;
							output.write(islaBegin.getBytes());
						}

						html += text;
						output.write(text.getBytes());

						if (run.isBold()) {
							html += boldEnd;
							output.write(boldEnd.getBytes());
						}
						if (run.isItalic()) {
							html += islaEnd;
							output.write(islaEnd.getBytes());
						}
						html += fontEnd;
						output.write(fontEnd.getBytes());
					}
				} catch (Exception e) {
					System.out.println("Write File Exception");
				}
			}
		}
	}

	private String decideColor(int a) {
		int color = a;
		switch (color) {
		case 1:
			return "#000000";
		case 2:
			return "#0000FF";
		case 3:
		case 4:
			return "#00FF00";
		case 5:
		case 6:
			return "#FF0000";
		case 7:
			return "#FFFF00";
		case 8:
			return "#FFFFFF";
		case 9:
			return "#CCCCCC";
		case 10:
		case 11:
			return "#00FF00";
		case 12:
			return "#080808";
		case 13:
		case 14:
			return "#FFFF00";
		case 15:
			return "#CCCCCC";
		case 16:
			return "#080808";
		default:
			return "#000000";
		}
	}

	public int decideSize(int size) {

		if (size >= 1 && size <= 8) {
			return 1;
		}
		if (size >= 9 && size <= 11) {
			return 2;
		}
		if (size >= 12 && size <= 14) {
			return 3;
		}
		if (size >= 15 && size <= 19) {
			return 4;
		}
		if (size >= 20 && size <= 29) {
			return 5;
		}
		if (size >= 30 && size <= 39) {
			return 6;
		}
		if (size >= 40) {
			return 7;
		}
		return 3;
	}

	private void makeFile() {
		String sdStateString = android.os.Environment.getExternalStorageState();
		if (sdStateString.equals(android.os.Environment.MEDIA_MOUNTED)) {
			try {
				File sdFile = android.os.Environment.getExternalStorageDirectory();

				String path = sdFile.getAbsolutePath() + File.separator + "xiaobaizu" + File.separator + "doc";

				File dirFile = new File(path);
				if (!dirFile.exists()) {
					dirFile.mkdirs();
				}

				File myFile = new File(path + File.separator + "doc.html");

				if (!myFile.exists()) {
					myFile.createNewFile();
				}

				htmlPath = myFile.getAbsolutePath();

			} catch (Exception e) {

			}
		}
	}

	private void getRange(File file) {
		FileInputStream in = null;
		POIFSFileSystem pfSystem = null;
		try {
			in = new FileInputStream(file);
			pfSystem = new POIFSFileSystem(in);
			hwpf = new HWPFDocument(pfSystem);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		range = hwpf.getRange();
		pictures = hwpf.getPicturesTable().getAllPictures();

		tableIterator = new TableIterator(range);
	}

	private void initTab() {
		menuTitle.setText("合同");
	}

	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		switch (v.getId()) {
		case R.id.menu_back:
			finish();
			overridePendingTransition(R.anim.cu_push_left_in, R.anim.cu_push_right_out);
			break;

		default:
			break;
		}
	}

}
