import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Arrays;
import java.io.File;
import java.util.Objects;


public class Main {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/pictures?autoReconnect=true&useSSL=false", "root", "Godzilla$31");
        StringBuilder query = new StringBuilder("insert into info values");
        PreparedStatement p = null;
        String img = "";
        boolean poggers = true;
        int errors = 0;
        int nulls = 0;
        int added = 0;
        File path = new File("C:\\Users\\WroX1\\Desktop\\properhashcodes.txt");
        FileWriter wr = new FileWriter(path, true);
        File dir = new File("C:\\Users\\WroX1\\Desktop\\yelan");
        for(String file : Objects.requireNonNull(dir.list())) {
            img = "C:\\Users\\WroX1\\Desktop\\yelan\\" + file;
            BufferedImage image = ImageIO.read(new File(img));
            if(image == null){
                nulls++;
                System.out.println("BIG L NULL");
                continue;
            }
            int[][] imagetag = get2darray(image);
            int pog = Arrays.deepHashCode(imagetag);
            String inter = String.valueOf(pog);
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(inter.getBytes());
            StringBuilder hexString = new StringBuilder();
            for(byte b : hash){
                hexString.append(String.format("%02x", b));
            }
            poggers = checker(String.valueOf(hexString));
            if (!poggers) {
                int height = image.getHeight();
                int width = image.getWidth();
                query.append(" (\"").append(hexString).append("\", \"").append(file).append("\", ").append(height).append(", ").append(width).append(")");
                p = con.prepareStatement(query.toString());
                p.execute();
                added++;
                wr.write(hexString + "\n" + file + "\n");
                movefile(img);
                query = new StringBuilder("insert into info values");
            } else {
                System.out.println(file + " is already in folder");
                errors++;
            }

            wr.flush();
        }
        wr.flush();
        wr.close();
        System.out.println("there were " + errors + " errors or duplicate photos ," + nulls + " null photos and added " + added + " photos" );
    }


    public static int[][] get2darray(BufferedImage image){
        int width = image.getWidth();
        int height = image.getHeight();
        int[][] result = new int[height][width];

        for(int row = 0; row < height; row++){
            for (int col = 0; col < width; col++){
                result[row][col] = image.getRGB(col, row);
            }
        }
        return result;
    }

    public static boolean checker(String inter) throws IOException, SQLException {
        boolean tf = false;
        Connection con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/pictures?autoReconnect=true&useSSL=false", "root", "Godzilla$31");
        PreparedStatement p = null;
        String sql = "select * from info where Picid = \"" + inter + "\" ";
        ResultSet rs = null;
        p = con.prepareStatement(sql);
        rs = p.executeQuery();
        tf = rs.next();
        return tf;
    }

    public static void movefile(String img){
        Path src = Paths.get(img);
        Path dest = Paths.get("C:\\Users\\WroX1\\Desktop\\yelanerfinal");
        try{
            Files.move(src, dest.resolve(src.getFileName()), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e){
            System.out.println("Exception while moving files: " + e.getMessage());
        }

    }
}
