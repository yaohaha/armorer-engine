package org.armorer.engine;

public class Ytlds {
    
    public Ytlds(){
        // 民兵 
        Soldier s1 = new Soldier(75,10,10,0,200,0);
        
        // 牛头怪 
        Soldier s2 = new Soldier(225,45,70,0,200,0);
        
        // 长弓手
        Soldier s3 = new Soldier(75,10,10,0,200,1200);
        
        s1.setNum(1000);
        s2.setNum(250);
        s3.setNum(30);
        
        // 牛头怪 
        Soldier d2 = new Soldier(225,45,70,0,200,0);
        
        // 戟兵
        Soldier d4 = new Soldier(150,40,40,0,300,0);
        
        d2.setNum(500);
        d4.setNum(500);
        
        // 第一回合
        // s民兵攻击d戟兵
        att1(s1,d4);
        System.out.println("剩余戟兵:"+d4.getNum());
        // s牛头怪 攻击d戟兵
        att1(s2,d4);
        System.out.println("剩余戟兵:"+d4.getNum());
        // s长弓手攻击d戟兵
        att1(s3,d4);
        System.out.println("剩余戟兵:"+d4.getNum());
        
        // d戟兵攻击s民兵
        att1(d4,s1);
        System.out.println("我方剩余民兵:"+s1.getNum());
        
        // d牛头攻击s民兵
        att1(d2,s1);
        System.out.println("我方剩余民兵:"+s1.getNum());
        
        
        // 第二回合
        // s民兵攻击d戟兵
        att1(s1,d4);
        System.out.println("剩余戟兵:"+d4.getNum());
        // s牛头怪 攻击d戟兵
        att1(s2,d4);
        System.out.println("剩余戟兵:"+d4.getNum());
        // s长弓手攻击d戟兵
        att1(s3,d4);
        System.out.println("剩余戟兵:"+d4.getNum());
        
        // d戟兵攻击s民兵
        att1(d4,s1);
        System.out.println("我方剩余民兵:"+s1.getNum());
        
        // d牛头攻击s民兵
        att1(d2,s1);
        System.out.println("我方剩余民兵:"+s1.getNum());
        
        
    }
    
    public static void main(String [] args){
        new Ytlds();
    }
    
    
    private void att1(Soldier s ,Soldier d){
       int sNum = s.getNum() ;
       // 生命值
      int sAttr3 =  s.getAttr3();
      // 敌方防御
      int dAttr2 = d.getAttr2();
      // 伤害
      int shanghai = sNum * sAttr3 * (1-dAttr2/1000);
      
      int dNum = d.getNum() ;
      // 生命值
     int dAttr1 =  d.getAttr1();
     // 防御
     int fangyu = dNum * dAttr1;

     int shengyu = ((fangyu - shanghai )/d.getAttr1());
     d.setNum(shengyu);
    }
    
    private void att2(Soldier s ,Soldier d){
        int sNum = s.getNum() ;
        // 生命值
       int sAttr3 =  s.getAttr4();
       // 敌方防御
       int dAttr2 = d.getAttr2();
       // 伤害
       int shanghai = sNum * sAttr3 * (1-dAttr2/1000);
       
       int dNum = d.getNum() ;
       // 生命值
      int dAttr1 =  d.getAttr1();
      // 防御
      int fangyu = dNum * dAttr1;

      int shengyu = ((fangyu - shanghai )/d.getAttr1());
      d.setNum(shengyu);
     }
}
