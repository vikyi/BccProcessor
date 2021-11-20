import java.io.*;

public class BccProcessor {
    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();

        FileReader fr = new FileReader("src/1.bcc");
        BufferedReader br = new BufferedReader(fr);
        String s = br.readLine(); // 下载的bcc原文件只有一行
        FileWriter fw = new FileWriter("src/2.txt");
        BufferedWriter bw = new BufferedWriter(fw);
        String[] result = s.split("},\\{");
        //处理第一段
        int lio = result[0].lastIndexOf("{") + 1;
        bw.write(new String(dialogue(result[0].substring(lio))));
        bw.flush();
        bw.newLine();
        //处理二到倒数第二段
        for (int i = 1; i < result.length - 1; i++) {
            bw.write(new String(dialogue(result[i])));
            bw.flush();
            bw.newLine();
        }
        //处理最后一段
        StringBuilder sb = dialogue(result[result.length - 1]);
        sb.delete(sb.length() - 3, sb.length());
        bw.write(new String(sb));
        //释放资源
        bw.close();
        br.close();

        long endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime);
    }

    //    计算每句台词开始/结束时间
    static StringBuilder cal(double d) {
        StringBuilder sb = new StringBuilder();
        int min = (int) d / 60;
        int hour = min / 60;
        double sec = d - min * 60 + 2.0;
        sec = (double) Math.round(sec * 100) / 100;
        sb.append(hour).append(":");
        if (min < 10)
            sb.append("0"); // 一位数的分补0
        sb.append(min).append(":");
        if (sec < 10.0)
            sb.append("0"); // 一位数的秒补0
        sb.append(sec);
        if (sb.length() != 10)
            sb.append("0"); // 整数的毫秒补0
        return sb;
    }

    static StringBuilder dialogue(String result) {
        String[] data;
        StringBuilder sb = new StringBuilder();
        StringBuilder script = new StringBuilder();
        data = result.split(",");
        double from = Double.parseDouble(data[0].substring(7));
        double to = Double.parseDouble(data[1].substring(5));
//        一种便于阅读的格式输出
//        sb.append(cal(from)).append("——").append(cal(to)).append("  ")
//                .append(data[3].substring(11, data[3].length() - 1));

//        处理成.ass格式，可直接复制
        script.append(data[3].substring(11, data[3].length() - 1));
        sb.append("Dialogue: 0,").append(cal(from)).append(",")
                .append(cal(to)).append(",");
        int index = script.indexOf("：");
        if (index != -1) { // 字幕中有冒号时，会设置字幕格式为冒号前的人物名（须自己再设置对应字体格式）
            sb.append(script.substring(0, index));
            sb.append(",,0,0,0,,");
            sb.append(script.substring(index + 1));
        } else { // 字幕中没有冒号时，会设置为default，default也须自己设置字体等
            sb.append("default");
            sb.append(",,0,0,0,,");
            sb.append(script);
        }

        return sb;
    }
}
