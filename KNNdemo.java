import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.*;

import javax.swing.*;

import java.lang.Math;

public class KNNdemo {

    private static final int k = 7;// k值为7，即根据最近7个样本点判断
    private static final int pointsNum = 17;
    // 样本点
    private static int[] knownPx = { 70, 160, 65, 150, 170, 100, 150, 350, 450, 320, 380, 400, 280, 220, 300, 290,
            390 };
    private static int[] knownPy = { 40, 55, 105, 180, 190, 250, 255, 60, 70, 190, 185, 230, 310, 390, 350, 450, 410 };
    private static int[] knownL = { 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3 };

    class RankedLink {
        float dist;
        int lable;
        int seq;
        RankedLink next = null;

        public RankedLink(int seq, float dist, int lable) {
            this.seq = seq;
            this.dist = dist;
            this.lable = lable;
        }
    }

    public static void main(String args[]) {
        KNNdemo knn = new KNNdemo();
        knn.draw();
        
    }

    private float[] addNewPoint(int x, int y) {
        float[] distance = new float[pointsNum];
        for (int i = 0; i < pointsNum; i++) {
            distance[i] = (float) Math.sqrt(Math.pow(x - knownPx[i], 2) + Math.pow(y - knownPy[i], 2));
            System.out.println(distance[i]);
        }
        return distance;
    }

    private RankedLink kthNearest(float[] distance) {
        RankedLink ranked = new RankedLink(0, 0, 0);// 链表头
        for (int i = 1; i < pointsNum; i++) {
            RankedLink newNode = new RankedLink(i, distance[i], knownL[i]);
            RankedLink temp = ranked;
            while (temp.next != null) {
                if (temp.next.dist <= distance[i])
                    temp = temp.next;
                else {
                    newNode.next = temp.next;
                    temp.next = newNode;
                    break;
                }
            }
            if (temp.next == null) {
                temp.next = newNode;
            }
        }
        return ranked;
    }

    private int mostLabel(RankedLink rl) {
        int l1 = 0, l2 = 0, l3 = 0;
        for (int i = 0; i < k; i++) {
            switch (rl.next.lable) {
                case 1:
                    l1++;
                    break;
                case 2:
                    l2++;
                    break;
                case 3:
                    l3++;
                    break;
            }
        }
        if (l1 > l2) {
            if (l1 > l3)
                return 1;
            else
                return 3;
        } else {
            if (l2 > l3)
                return 2;
            else
                return 3;
        }
    }

    private void draw() {
        // JPanel jp = new JPanel();
        JFrame window = new JFrame("knn实验");
        window.setBounds(400, 100, 600, 500);
        knnPanel kp = new knnPanel();
        kp.addMouseListener(new MouseListener(){
            
            public void mouseClicked(MouseEvent e){
                int x = e.getX(),y=e.getY();
                float[] distance = addNewPoint(x, y);
                RankedLink rl = kthNearest(distance);
                int label = mostLabel(rl);
                Graphics g = kp.getGraphics();
                switch(label){
                    case 1:g.setColor(Color.BLUE);break;
                    case 2:g.setColor(Color.ORANGE);break;
                    case 3:g.setColor(Color.RED);break;
                }
                g.fillOval(x, y, 5, 5);
            }
            public void mousePressed(MouseEvent e){}
            public void mouseReleased(MouseEvent e){}
            public void mouseEntered(MouseEvent e){}
            public void mouseExited(MouseEvent e){}
        });

        window.add(kp);

        window.setVisible(true);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }

    static class knnPanel extends JPanel {
        @Override
        public void paint(Graphics g) {
            super.paint(g);
            // 已知点
            for (int i = 0; i < pointsNum; i++){
                switch(knownL[i]){
                    case 1:g.setColor(Color.BLUE);break;
                    case 2:g.setColor(Color.ORANGE);break;
                    case 3:g.setColor(Color.RED);break;
                }
                g.fillOval(knownPx[i], knownPy[i], 5, 5);
            }
            
        }
    }

}