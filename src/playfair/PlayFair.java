/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package playfair;

import java.util.Locale;
import java.util.Scanner;

/**
 *
 * @author James Gagnon
 */
public class PlayFair {
static StringBuilder sb1, sb;
static int count = 0; 
static char[][] table = new char [5][5];
    
public static void main(String[] args) {

Scanner input = new Scanner(System.in);

int count2 = 0;
boolean contI = false;
boolean contJ = false;
int command = 0;
String message;
System.out.println("Enter a keyword");
String keyword = input.nextLine();
keyword = keyword.toUpperCase(Locale.ENGLISH);
System.out.println("Keyword entered is: "+ keyword);
 sb = new StringBuilder(keyword);

//check keyword for duplicate characters
for(int i = 0; i <keyword.length();i++){
    for (int j = 0; j< keyword.length();j++){
        if(keyword.charAt(j)==keyword.charAt(i) && j != i){
            sb.deleteCharAt(i);
            keyword = sb.toString();
        }
    }
}

System.out.println("Duplicate characters removed, new keyword is: "+ keyword);

System.out.println("Generating Alphabet Matrix Fill");
System.out.println();
char[] alpha = new char[26];
char[] alpha2 = new char[alpha.length-keyword.length()];
for (char letter = 'A'; letter <='Z';letter++){
    alpha[count]=letter;
     count++;
}
count = 0;
for(int i = 0;i<alpha.length;i++){
    for(int j =0; j<keyword.length();j++){
        if(keyword.charAt(j)== ('I')||keyword.charAt(j)=='J'){
            alpha[8] = '2';
            alpha[9] = '2';
        }
        if(alpha[i]==keyword.charAt(j)){
           alpha[i]='2';
           
        }  
     
    }
    alpha[9]='2'; //Discard J as I/J are equal.
    // System.out.print(alpha[i] + " ");
}

for(int i = 0; i <alpha.length;i++){
    if (alpha[i]=='2'){}
    else {alpha2[count]=alpha[i];
   // System.out.print(alpha2[count]+ " ");
    count++;
    }
}

count = 0;
count2 = 0;
for(int i = 0; i<5 ;i++){       //Populate table with keyword and alphabet
    for(int j = 0; j<5; j++){
       if(count < keyword.length()){
           table[i][j] = keyword.charAt(count);
             
       }
      count++;

        if (count >keyword.length()){
        table[i][j] = alpha2[count2];
        count2++;
        }
        
       System.out.print(table[i][j]+" ");
    }
System.out.println();
}
    System.out.println();
    System.out.println("Enter a Message to Encrypt");
    message = input.nextLine();
    message = message.toUpperCase();
     sb1 = new StringBuilder(message);
     message = checkSpace(message);
     message = appendX(message);
     sb1.delete(0,2000);
     sb1 = new StringBuilder(message);
    if(message.length()%2 != 0){
        System.out.println("Number of characters is odd, appending X to end of message.");
        sb1.append('X');  //add an X to odd number of characters
    }
    message = sb1.toString();
    System.out.println("Message is set to "+ message);
    System.out.println("Decomposing message.");
    sb1.delete(0, message.length()); //clear StringBuilder for reuse.
 //break message into segments
  
String plainText = " ";
String cipherText = " ";

    cipherText = cipher(message);
    System.out.println("Encrypted Message is: "+cipherText);
System.out.println("Decrypted Message is: "+decrypt(message));
}

public static String appendX(String text){
    String result;
    sb = new StringBuilder();
    for(int i = 0; i <text.length();i++){
        
        if(i%2==0&& i<text.length()-1){
            if(text.charAt(i)==text.charAt(i+1)){
                System.out.println("Duplicates found, inserting X.");
                sb.append(text.charAt(i));
                sb.append('X');
                sb.append(text.charAt(i+1));
            }
            else {sb.append(text.charAt(i));
            sb.append(text.charAt(i+1));}
        }
        
    }
//sb.append(text.charAt(text.length()-1));
    result = sb.toString();
    sb.delete(0, 2000);
    System.out.println("Appended String is "+result);
    return result;
}
public static String checkSpace(String message){
      sb1.delete(0, 2000);
     char[] message1 = message.toCharArray();
     for(int i=0;i<message.length();i++){
    if (message1[i]== ' '){message1[i]='X';
   }
     sb1.append(message1[i]);}
     String result = sb1.toString();
     sb1.delete(0, 2000);
     return result;
}
    public static String[]decompose(String message){      
          char[] message1 = message.toCharArray();
          char temp, temp2;
    String[] message2 = new String[message.length()/2]; //group chars in pairs of 2.
    count = 0;
    for(int i = 0; i <message1.length;i++){
        if(i%2 == 0){
            

           
            sb1.append(message1[i]);
            sb1.append(message1[i+1]);
            

            message2[count]=sb1.toString();
            sb1.delete(0, 2000); //clear up to 2000 characters in StringBuilder
            count++;
        }
    }
    return message2;
    }
    public static String cipher(String message){
System.out.println("Beginning Encryption");
String []message2 = decompose(message);
for(int i = 0; i <message2.length;i++){
    if(checkRow(table, message2[i])==true){
    sb1.append(encryptRow(table,message2[i]));
    }
    else if(checkColumn(table, message2[i])==true){
        sb1.append(encryptColumn(table,message2[i]));
    }
    else {sb1.append(encrypt(table,message2[i]));}
}
String cipherText = sb1.toString();
sb1.delete(0, 2000);
return cipherText;
    }
    public static String decrypt(String message){
       String []message2 = decompose(message);
       String plainText, plainText1;

for(int i = 0; i <message2.length;i++){
    if(checkRow(table, message2[i])==true){
    sb1.append(decryptRow(table,message2[i]));
    }
    else if(checkColumn(table, message2[i])==true){
        sb1.append(decryptColumn(table,message2[i]));
    }
    else {sb1.append(encrypt(table,message2[i]));}
}

plainText = sb1.toString();


sb1.delete(0, 2000);
return plainText;
    }
    
    public static boolean checkRow(char[][] table, String x){
               char[] result = new char[2];
        char[] string = x.toCharArray();
        boolean match = false;
        for(int i = 0; i <table.length;i++){
        
        for(int j = 0; j<table.length;j++){
            if (string[0]==table[i][j]){

                for(int z = 0; z<table.length;z++){
                    if (string[1]==table[i][z]){
                
                        match = true;
                    }
                }
            }
        }   
    }
         return match;
    }
    
    public static char[]encryptRow(char[][]table, String x){
        char[] result = new char[2];
        char[] string = x.toCharArray();
        int x1=0;
        int x2 = 0;
        int y1=0;
        boolean match = false;
        for(int i = 0; i <table.length;i++){
        
        for(int j = 0; j<table.length;j++){
            if (string[0]==table[i][j]){
                x1=i;
                x2=j;
                for(int z = 0; z<table.length;z++){
                    if (string[1]==table[i][z]){
                        y1=z;
                        match = true;
                    }
                }
            }
        }  
      
        }
        if(x2+1>4){x2 =-1;}
        if(y1+1>4){y1 =-1;}
        result[0]=table[x1][x2+1];
        result[1]= table[x1][y1+1];
       // System.out.println("Coordinates located at "+x1+","+x2+" "+x1+","+y1);
       // System.out.println();
        if(match == true){
        //System.out.println("Encrypted result is: "+result[0]+result[1]);
        return result;}
        else
           // System.out.println("No row match for "+x);
        return string;
    }
        public static boolean checkColumn(char[][]table, String x){
        char[] result = new char[2];
        char[] string = x.toCharArray();
        int x1=0;
        int x2 = 0;
        int y1=0;
        boolean match = false;
       
                for(int j = 0; j <table.length;j++){
        
        for(int i = 0; i<table.length;i++){
            if (string[0]==table[i][j]){
                x1=i;
                x2=j;
                for(int z = 0; z<table.length;z++){
                    if (string[1]==table[z][j]){
                        y1=z;
                        match = true;
                    }
                    
                }
            }
        } 
        }
                return match;
        }
               
    public static char[]encryptColumn(char[][]table, String x){
        char[] result = new char[2];
        char[] string = x.toCharArray();
        int x1=0;
        int x2 = 0;
        int y1=0;
        boolean match = false;
       
                for(int j = 0; j <table.length;j++){
        
        for(int i = 0; i<table.length;i++){
            if (string[0]==table[i][j]){
                x1=i;
                x2=j;
                for(int z = 0; z<table.length;z++){
                    if (string[1]==table[z][j]){
                        y1=z;
                        match = true;
                    }
                    
                }
            }
        }  
      
        }
        if(x1+1>4){x1 =-1;}
        if(y1+1>4){y1 =-1;}
        result[0]=table[x1+1][x2];
        result[1]= table[y1+1][x2];
       // System.out.println("Coordinates located at "+x1+","+x2+" "+x1+","+y1);
        //System.out.println();
        
        if (match == true){
       // System.out.println("Encrypted result is: "+result[0]+result[1]);
        return result;}
        else
           // System.out.println("No column match for "+ x);
            return string;
    }
        public static char[]encrypt(char[][]table, String x){
        char[] result = new char[2];
        char[] string = x.toCharArray();
        int x1=0;
        int x2 = 0;
        int y1=0;
        int y2 = 0;
       
       
                for(int i = 0; i <table.length;i++){
        
        for(int j = 0; j<table.length;j++){
            if (string[0]==table[i][j]){
                x1=i;
                x2=j;

            }
        }  
            for(int z = 0; z<table.length;z++){
               if (string[1]==table[z][i]){
                    y1=z;
                    y2 = i;
                      
                    }
                    
                }
        }

        result[0]=table[y1][x2];
        result[1]= table[x1][y2];
       // System.out.println("Coordinates located at "+x1+","+x2+" "+x1+","+y1);
      
        
       
        //System.out.println("Encrypted result is: "+result[0]+result[1]);
        return result;}
        
        public static char[]decryptRow(char[][]table, String x){
        char[] result = new char[2];
        char[] string = x.toCharArray();
        int x1=0;
        int x2 = 0;
        int y1=0;
        boolean match = false;
        for(int i = 0; i <table.length;i++){
        
        for(int j = 0; j<table.length;j++){
            if (string[0]==table[i][j]){
                x1=i;
                x2=j;
                for(int z = 0; z<table.length;z++){
                    if (string[1]==table[i][z]){
                        y1=z;
                        match = true;
                    }
                }
            }
        }  
      
        }
        if(x2==0){x2 =5;}
        if(y1==0){y1 =5;}
        result[0]=table[x1][x2-1];
        result[1]= table[x1][y1-1];
       // System.out.println("Coordinates located at "+x1+","+x2+" "+x1+","+y1);
       // System.out.println();
        if(match == true){
        //System.out.println("Encrypted result is: "+result[0]+result[1]);
        return result;}
        else
           // System.out.println("No row match for "+x);
        return string;
    }
    public static char[]decryptColumn(char[][]table, String x){
        char[] result = new char[2];
        char[] string = x.toCharArray();
        int x1=0;
        int x2 = 0;
        int y1=0;
        boolean match = false;
       
                for(int j = 0; j <table.length;j++){
        
        for(int i = 0; i<table.length;i++){
            if (string[0]==table[i][j]){
                x1=i;
                x2=j;
                for(int z = 0; z<table.length;z++){
                    if (string[1]==table[z][j]){
                        y1=z;
                        match = true;
                    }
                    
                }
            }
        }  
      
        }
        if(x1==0){x1 =5;}
        if(y1==0){y1 =5;}
        result[0]=table[x1-1][x2];
        result[1]= table[y1-1][x2];
       // System.out.println("Coordinates located at "+x1+","+x2+" "+x1+","+y1);
        //System.out.println();
        
        if (match == true){
       // System.out.println("Encrypted result is: "+result[0]+result[1]);
        return result;}
        else
           // System.out.println("No column match for "+ x);
            return string;
    }
    }

