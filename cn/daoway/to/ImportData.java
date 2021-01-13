package cn.daoway.to;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ImportData {
    public static void main(String[] args) throws IOException, BiffException, SQLException, ClassNotFoundException {
        File file = new File("E:\\1.xls");
        importData(file);
    }

    public static void importData(File path) throws IOException, BiffException, SQLException, ClassNotFoundException {
        //读取Excel
        Workbook workbook = Workbook.getWorkbook(path);
        Sheet[] sheets = workbook.getSheets();
        GetMap gm = new GetMap();
        Connection conn = gm.getDbConection();
        PreparedStatement pstatement = null;
        if (sheets != null)
            for (Sheet sheet : sheets) {
                //获取行数
                int rows = sheet.getRows();
                System.out.println(rows);
                //获取列数
                int cols = sheet.getColumns();
                System.out.println(cols);
                //读取数据
                for (int row = 1; row < rows; row++)  //z这里row从2开始是因为去除了表头占的两行
                {
                    String values[] = new String[8];
                    for (int col = 0; col < cols; col++) {
                        //将每行不同列的内容放入数组
                        values[col] = sheet.getCell(col, row).getContents();
                    }
                    //将读取出来的内容写入mysql数据库
                    try {
                        pstatement = conn.prepareStatement("insert t_user_info values(?,?,?,?,?,?,?,?,?,?);");
                        pstatement.setNString(1, values[0]);
                        pstatement.setNString(2, values[1]);
                        pstatement.setNString(3, values[2]);
                        pstatement.setNString(4, values[3]);
                        pstatement.setNString(5, values[4]);
                        pstatement.setNString(6, values[5]);
                        pstatement.setNString(7, values[6]);
                        pstatement.setNString(8, values[7]);
                        pstatement.setNString(9, values[8]);
                        /*pstatement.setNString(10, values[9]);*/
                        pstatement.executeUpdate();  //执行sql语句插入内容
                    } catch (SQLException e) {

                        e.printStackTrace();
                    }
                }
            }
        workbook.close();
        try {
            if (pstatement != null) {
                pstatement.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

