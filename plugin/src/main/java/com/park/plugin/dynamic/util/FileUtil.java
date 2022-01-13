package com.park.plugin.dynamic.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Objects;

import android.content.Context;

public class FileUtil {

  public static void deleteDir(File file){
    if(!file.exists()){
      return;
    }
    if(file.isDirectory()){
      for(File subFile : file.listFiles()){
        deleteDir(subFile);
      }
    }
    file.deleteOnExit();
  }

  public static long copy(final InputStream inputStream, final OutputStream outputStream) {
    try {
      return copyLarge(inputStream, outputStream, new byte[8192]);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return -1;
  }

  /**
   * Copies bytes from a large (over 2GB) {@code InputStream} to an
   * {@code OutputStream}.
   * <p>
   * This method uses the provided buffer, so there is no need to use a
   * {@code BufferedInputStream}.
   * </p>
   *
   * @param inputStream  the {@code InputStream} to read.
   * @param outputStream the {@code OutputStream} to write.
   * @param buffer       the buffer to use for the copy
   * @return the number of bytes copied.
   * @throws NullPointerException if the InputStream is {@code null}.
   * @throws NullPointerException if the OutputStream is {@code null}.
   * @throws IOException          if an I/O error occurs.
   * @since 2.2
   */
  @SuppressWarnings("resource") // streams are closed by the caller.
  public static long copyLarge(final InputStream inputStream, final OutputStream outputStream,
      final byte[] buffer)
      throws IOException {
    Objects.requireNonNull(inputStream, "inputStream");
    Objects.requireNonNull(outputStream, "outputStream");
    long count = 0;
    int n;
    while (-1 != (n = inputStream.read(buffer))) {
      outputStream.write(buffer, 0, n);
      count += n;
    }
    return count;
  }

  public static String getFileFirstLine(File file) throws Throwable {
    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
    return reader.readLine();
  }

  public static void clearAndWriteFile(File file, String line) throws Throwable {
    file.deleteOnExit();
    file.createNewFile();
    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
    writer.write(line);
    writer.flush();
    writer.close();

  }

  public static void copyAssetToCache(Context context, String assetFileName) {
    try {
      File cacheDir = context.getCacheDir();
      if (!cacheDir.exists()) {
        cacheDir.mkdirs();
      }
      File outFile = new File(cacheDir, assetFileName);
      if (!outFile.exists()) {
        outFile.createNewFile();
      }
      InputStream inputStream = context.getAssets().open(assetFileName);
      OutputStream outputStream = new FileOutputStream(outFile);
      copy(inputStream, outputStream);
      outputStream.flush();
      outputStream.close();
      inputStream.close();
    } catch (Throwable e) {
      e.printStackTrace();
    }
  }

  public static File createNewFileInCache(Context context, String fileName) {
    try {
      File cacheDir = context.getCacheDir();
      if (!cacheDir.exists()) {
        cacheDir.mkdirs();
      }
      File outFile = new File(cacheDir, fileName);
      outFile.deleteOnExit();
      outFile.createNewFile();
      return outFile;
    } catch (Throwable e) {
      e.printStackTrace();
    }
    return null;
  }
}
