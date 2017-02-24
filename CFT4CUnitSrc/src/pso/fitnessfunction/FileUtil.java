package pso.fitnessfunction;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.List;

public class FileUtil {

    public static String getBatPath(String filename) {
        return "\"" + filename.replace("\\", "\\\\") + "\"";
    }

    public static long getFileSize(String filename) {
        File file = new File(filename);
        if (!file.exists() || !file.isFile()) {
            return -1;
        }
        return file.length();
    }

    public static void removeFolder(String foldername) {
        File folder = new File(foldername);
        if (folder.exists() && folder.isDirectory()) {
            FileUtil.removeFolder(folder);
        }
    }

    public static void removeFolder(File folder) {
        File[] files = folder.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                files[i].delete();
            } else {
                FileUtil.removeFolder(files[i]);
            }
        }
        folder.delete();
    }

    public static String readData(String filename) {
        File file = new File(filename);
        return FileUtil.readData(file);
    }

    public static String readData(File file) {
        String string = "";
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            String str = null;
            while ((str = in.readLine()) != null) {
                string += str + "\n";
            }
            in.close();
        } catch (UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return string;
    }

    public static boolean writeData(String filename, String text) {
        File file = new File(filename);
        return FileUtil.writeData(file, text);
    }

    public static boolean writeData(File file, String text) {
        try {
            Writer out = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            out.write(text.trim());
            out.close();
            return true;
        } catch (IOException ex) {
        }
        return false;
    }

    public static boolean writeByteData(String filename, String text) {
        try {
            FileOutputStream fos = new FileOutputStream(filename);
            fos.write(text.getBytes());
            fos.close();
            return true;
        } catch (IOException ex) {
        }
        return false;
    }

    public static boolean writeByteData(String filename, List<byte[]> byteList) {
        try {
            FileOutputStream fos = new FileOutputStream(filename);
            for (int i = 0; i < byteList.size(); i++) {
//                fos.write(byteList.get(i));
                byte[] bytes= byteList.get(i);
                for (byte aByte : bytes) {
                    fos.write(aByte);
                }
            }
            fos.close();
            return true;
        } catch (IOException ex) {
        }
        return false;
    }

    public static boolean nioCopyData(java.io.File fileFrom, java.io.File fileTo) {
        if (!fileTo.exists()) {
            try {
                fileTo.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        try {
            java.nio.channels.FileChannel desChannel = new java.io.FileOutputStream(fileTo.getAbsolutePath()).getChannel();
            java.nio.channels.FileChannel srcChannel = new java.io.FileInputStream(fileFrom.getAbsolutePath()).getChannel();
            srcChannel.transferTo(0, srcChannel.size(), desChannel);
            desChannel.close();
            srcChannel.close();
            return true;
        } catch (java.io.FileNotFoundException fnfe) {
            fnfe.printStackTrace();
            return false;
        } catch (java.io.IOException ioe) {
            ioe.printStackTrace();
            return false;
        }
    }

    public static String nioReadData(java.io.File file) {
        try {
            java.io.FileInputStream finStream = new java.io.FileInputStream(file);
            java.nio.channels.FileChannel fileChannel = finStream.getChannel();
            java.nio.ByteBuffer byteBuffer = java.nio.ByteBuffer.allocate((int) fileChannel.size());
            fileChannel.read(byteBuffer);
            byteBuffer.rewind();
            String str = new String(java.nio.charset.Charset.forName("UTF-8").decode(byteBuffer).array());
            finStream.close();
            fileChannel.close();
            return str;
        } catch (java.io.FileNotFoundException fnfe) {
            System.out.println(fnfe.getMessage());
            return null;
        } catch (java.io.IOException ioe) {
            System.out.println(ioe.getMessage());
            return null;
        }
    }
}
