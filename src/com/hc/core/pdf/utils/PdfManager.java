// package com.hc.core.pdf.utils;
//
// import java.io.File;
// import java.io.FileNotFoundException;
// import java.io.FileOutputStream;
// import java.io.IOException;
// import java.util.ArrayList;
//
// import android.util.Log;
//
// import com.itextpdf.text.Document;
// import com.itextpdf.text.DocumentException;
// import com.itextpdf.text.Image;
// import com.itextpdf.text.PageSize;
// import com.itextpdf.text.Paragraph;
// import com.itextpdf.text.pdf.PdfWriter;
//
// public class PdfManager {
// public static File Pdf(ArrayList<String> imageUrllist,String
// mOutputPdfFileName) {
// String TAG = "PdfManager";
// Document doc = new Document(PageSize.A4, 20, 20, 20, 20);
// try {
// PdfWriter
// .getInstance(doc, new FileOutputStream(mOutputPdfFileName));
// doc.open();
// for (int i = 0; i < imageUrllist.size(); i++) {
// doc.newPage();
// doc.add(new Paragraph("��ʹ��iText"));
// Image png1 = Image.getInstance(imageUrllist.get(i));
// float heigth = png1.getHeight();
// float width = png1.getWidth();
// int percent = getPercent2(heigth, width);
// png1.setAlignment(Image.MIDDLE);
// png1.scalePercent(percent+3);// ��ʾ��ԭ��ͼ��ı���;
// doc.add(png1);
// }
// doc.close();
// } catch (FileNotFoundException e) {
// e.printStackTrace();
// } catch (DocumentException e) {
// e.printStackTrace();
// } catch (IOException e) {
// e.printStackTrace();
// }
//
// File mOutputPdfFile = new File(mOutputPdfFileName);
// if (!mOutputPdfFile.exists()) {
// Log.i(TAG, "���ɵ�Pdfʧ��");
// mOutputPdfFile.deleteOnExit();
// return null;
// }
// return mOutputPdfFile;
// }
//
// /**
// * ��һ�ֽ������
// �ڲ��ı�ͼƬ��״��ͬʱ���жϣ����h>w����hѹ����������w>h��w=h������£������ѹ��
// *
// * @param h
// * @param w
// * @return
// */
//
// public static int getPercent(float h, float w) {
// int p = 0;
// float p2 = 0.0f;
// if (h > w) {
// p2 = 297 / h * 100;
// } else {
// p2 = 210 / w * 100;
// }
// p = Math.round(p2);
// return p;
// }
//
// /**
// * �ڶ��ֽ��������ͳһ���տ��ѹ��
// ��������Ч���ǣ�����ͼƬ�Ŀ������ȵģ�������Ϊ���ͻ���Ч������õ�
// *
// * @param args
// */
// public static int getPercent2(float h, float w) {
// int p = 0;
// float p2 = 0.0f;
// p2 = 530 / w * 100;
// p = Math.round(p2);
// return p;
// }
// }