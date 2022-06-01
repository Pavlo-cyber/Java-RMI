import java.rmi.*;
import java.rmi.registry.*;
import java.io.*;
import java.rmi.server.*;
import java.util.*;

public class lab_4
{
    public interface Brain extends Remote 	//Це віддалений інтерфейс
    {
        public double[][] MultiplyMatrix(double[][] m1,double[][] m2,int row1,int row2,int col1,int col2) throws Exception;

       public void PrintMatrix(double[][] m,int n) throws  Exception;

        public double[][] GenerateMatrix(int n) throws Exception;

        public double[][] SumMatrix(double[][] m1,double[][] m2,int row,int col) throws Exception;

        public double[][] MultiplyByConst(double[][] m,int val,int row,int col) throws Exception;

        public double[] SubtractRows(double[] m1,double[] m2,int n) throws Exception;

        public double[] transpose(double[][] m,int n) throws Exception;

        public double[] MultiplyRowOnMatrix(double[] a,double[][] m,int row,int col) throws Exception;

        public double[] SumRow(double[] a1,double[] a2,int n) throws Exception;
        public double[][] GenerateCol(int n) throws Exception;

        public double[][] CalculateC(int n)  throws Exception;

        public double[][] calculate_b(int n) throws Exception;

        public double[][] ColByRow(double[][] m,double[] a,int row,int col) throws Exception;

        public double[] AddConst(double[] a1, double val,int n) throws Exception;

    }
    public class BrainImpl extends UnicastRemoteObject implements Brain//Це реалізація віддаленого інтерфейсу
    {
        public BrainImpl() throws Exception {
        }

        public double[][] MultiplyMatrix(double[][] m1, double[][] m2, int row1, int row2, int col1, int col2) throws Exception
        {
            double[][] res = new double[row1][col2];
            for (int i = 0; i < row1; i++) {
                for (int j = 0; j < col2; j++) {
                    for (int k = 0; k < row2; k++) {
                        res[i][j] += m1[i][k] * m2[k][j];
                    }
                }
            }
            return  res;
        }

        public void PrintMatrix(double[][] m,int n) throws Exception
        {
            for(int i=0;i<n;i++)
            {
                for(int j=0;j<n;j++)
                {
                    System.out.print("["+ m[i][j] +"] ");
                }
                System.out.println("");
            }
        }

        public double[][] GenerateMatrix(int n) throws Exception
        {
            double res[][]=new double[n][n];
            Random r = new Random();
            int low = 10;
            int high = 100;
            for(int i=0;i<n;i++)
            {
                for(int j=0;j<n;j++)
                {
                    res[i][j]=r.nextInt(high-low)+low;
                }
            }
            return res;
        }

        public double[][] GenerateCol(int n) throws Exception
        {
            double[][] res=new double[n][1];
            Random r = new Random();
            int low = 10;
            int high = 100;
            for(int i=0;i<n;i++)
            {
                res[i][0]=r.nextInt(high-low)+low;
            }
            return res;
        }

        public double[][] SumMatrix(double[][] m1,double[][] m2,int row,int col) throws Exception
        {
            double [][] res=new double[row][col];
            for(int i=0;i<row;i++)
            {
                for(int j=0;j<col;j++)
                {
                    res[i][j]=m1[i][j]+m2[i][j];
                }
            }
            return res;
        }
        public double[][] MultiplyByConst(double[][] m, int val,int row,int col) throws Exception
        {
            double [][] res=new double[row][col];
            for(int i=0;i<row;i++)
            {
                for(int j=0;j<col;j++)
                {
                    res[i][j]=m[i][j] *val;
                }
            }
            return res;
        }
        public double[] SubtractRows(double[] m1,double[] m2,int n) throws Exception
        {
            double[] res=new double[n];
            for(int i=0;i<n;i++)
            {
                res[i]=m1[i]-m2[i];
            }
            return res;
        }
        public double[] MultiplyRowByConst(double[] m,int val,int n) throws Exception
        {
            double[] res=new double[n];
            for(int i=0;i<n;i++)
            {
                res[i]=m[i] *val;
            }
            return res;
        }

        public double[][] CalculateC(int n)  throws Exception
        {
            double[][] res= new double[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    res[i][j] = 1.0 / (Math.pow(i + 1, 2) + j + 1);
                }
            }
            return res;
        }

        public double[][] calculate_b(int n) throws Exception
        {
            double[][] res=new double[n][1];
            for(int i=0;i<n;i++)
            {
                res[i][0]=5*Math.pow(i+1,3);
            }
            return res;
        }

        public double[] transpose(double[][] m,int n) throws Exception
        {
            double[] res=new double[n];
            for(int i=0;i<n;i++)
            {
                res[i]=m[i][0];
            }
            return res;
        }

        public double[][] ColByRow(double[][] m,double[] a,int row,int col) throws Exception
        {
            double[][] res=new double[row][col];
            for(int i=0;i<col;i++)
            {
                for(int j=0;j<row;j++)
                {
                    res[i][j]=m[i][0] * a[j];
                }
            }
            return res;
        }
        public double[] MultiplyRowOnMatrix(double[] a,double[][] m,int row,int col) throws Exception
        {
            double[] res=new double[col];
            for(int i=0;i<col;i++)
            {
                for(int j=0;j<row;j++)
                {
                    res[i]+=a[j]*m[j][i];
                }
            }
            return res;
        }

       public double[] SumRow(double[] a1,double[] a2,int n) throws Exception
        {
            double[] res=new double[n];
            for(int i=0;i<n;i++)
            {
                res[i]=a1[i] +a2[i];
            }
            return res;
        }

        public double[] AddConst(double[] a1, double val,int n) throws Exception
        {
            double[] res=new double[n];
            for(int i=0;i<n;i++)
            {
                res[i]=a1[i] +val;
            }
            return res;
        }
    }

        public class Host	//Це сервер
    {
        public Registry reg;
        public Host(String HostName,int port) throws Exception
        {
            Registry reg = LocateRegistry.createRegistry(port);	//Порт задеє користувач
            reg.rebind(HostName, new BrainImpl());	//Прив'язую сервер до реєстру RMI
            System.out.println("Host "+HostName+" started successfully");
            System.out.println("Press [Enter] to close the Host");
            BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
            keyboard.readLine();	//Сервер завершить роботу при натиску клавіші Enter
        }
    }
    public class Client	//Це клієнт
    {
        public Brain remotingBrain;
        public Client(String address,String HostName,int port) throws Exception
        {
            remotingBrain = (Brain) Naming.lookup("//"+address+":"+port+"/"+HostName);
            System.out.println("Connection to "+address+":"+port+"/"+HostName+" is succeed");
        }
        public void PrintMatrix(double[][] m,int row,int col)
        {
            for(int i=0;i<row;i++)
            {
                for(int j=0;j<col;j++)
                {
                    System.out.print("["+ m[i][j] +"] ");
                }
                System.out.print("\n");
            }
        }
        public void PrintRow(double[] a,int n)
        {
            for(int i=0;i<n;i++)
            {
                System.out.print("["+ a[i] +"] ");
            }
            System.out.print("\n");
        }
    }
    public lab_4() throws Exception	//Це реалізація інтерфейсу користувача
    {
        double [][] A;
        double [][] A1;
        double [][] A2;
        double [][] B;
        double [][] C;

        double[][] b;
        double[][] b1;
        double[][] c1;
        double[][] y1;
        double[][] Y3;
        double[][] y2;

        double[] temp;
        double[][] temp2;
        double[][] temp3;
        double[] temp4;
        double[][] temp5;
        double[][] temp6;
        double[] temp7;
        double[][] temp8;
        double[][] temp9;
        double[][] temp10;
        double[] x;


        System.out.println("Type [1] to start Host");
        System.out.println("Type [2] to start Client");
        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
        int c = keyboard.readLine().charAt(0); //Вибір запуску клієнта або сервера
        if (c == '1')	//Якщо обрано запуск сервера
        {
            String address = null;
            String name = null;
            int port;
            System.out.print("\t-host name:\t");
            name = keyboard.readLine();	//Зчитую ім'я сервера
            System.out.print("\t-port:\t\t");
            port = Integer.parseInt(keyboard.readLine());	//І номер порта де він буде розміщений
            System.out.println("\tHost is starting ...");
            Host myHost = new Host(name,port);	//Запускаю сервер
        }
        else if (c == '2')	//Якщо обрано запуск клієнта
        {
            String address = null;
            String name = null;
            int port;
            System.out.print("\t-adress:\t");
            address = keyboard.readLine();	//Зчитую адрес де має бути розташований сервер
            System.out.print("\t-host name:\t");
            name = keyboard.readLine();		//Зчитую його ім'я
            System.out.print("\t-port:\t\t");
            port = Integer.parseInt(keyboard.readLine());	//І його порт
            System.out.println("\tClient is starting...");
            System.out.print("\tType matrix size ");
            final double startTime = System.currentTimeMillis();
            int n=Integer.parseInt(keyboard.readLine());
            A=new double[n][n];
            A1=new double[n][n];
            A2=new double[n][n];
            B=new double[n][n];
            C=new double[n][n];
            b=new double[n][1];
            b1=new double[n][1];
            c1=new double[n][1];
            y1=new double[n][1];
            Y3=new double[n][n];
            y2=new double[n][1];
            temp=new double[n];
            temp2=new double[n][n];
            temp3=new double[n][1];
            temp4=new double[1];
            temp5=new double[n][1];
            temp6=new double[n][n];
            temp7=new double[n];
            temp8=new double[n][n];
            temp9=new double[n][1];
            temp10=new double[n][1];
            x=new double[1];
            Client myClient = new Client(address,name,port);//Запускаю клієнт
            A=myClient.remotingBrain.GenerateMatrix(n);
            A1=myClient.remotingBrain.GenerateMatrix(n);
            A2=myClient.remotingBrain.GenerateMatrix(n);
            B=myClient.remotingBrain.GenerateMatrix(n);
            C=myClient.remotingBrain.CalculateC(n);

            b=myClient.remotingBrain.calculate_b(n);
            b1=myClient.remotingBrain.GenerateCol(n);
            c1=myClient.remotingBrain.GenerateCol(n);

            y1=myClient.remotingBrain.MultiplyMatrix(A,b,n,n,n,1);
            System.out.println("y1= ");
            myClient.PrintMatrix(y1,n,1);

            System.out.println("Y3= ");
            Y3=myClient.remotingBrain.MultiplyMatrix(A2, myClient.remotingBrain.SumMatrix(B,myClient.remotingBrain.MultiplyByConst(C,10,n,n),n,n),n,n,n,n);
            myClient.PrintMatrix(Y3,n,n);
            System.out.println("y2= ");
            y2=myClient.remotingBrain.MultiplyMatrix(A1,myClient.remotingBrain.SumMatrix( myClient.remotingBrain.MultiplyByConst(b1,5,n,1),c1,n,1),n,n,n,1);
            myClient.PrintMatrix(y2,n,1);

            temp=myClient.remotingBrain.MultiplyRowOnMatrix(myClient.remotingBrain.transpose(y1,n),Y3,n,n);

            temp2=myClient.remotingBrain.MultiplyMatrix(Y3,Y3,n,n,n,n);

            temp3=myClient.remotingBrain.MultiplyMatrix(Y3,y2,n,n,n,1);

            temp4=myClient.remotingBrain.MultiplyRowOnMatrix(temp,y1,n,1);

            temp5=myClient.remotingBrain.SumMatrix(temp3,y1,n,1);

            temp6=myClient.remotingBrain.ColByRow(y1,myClient.remotingBrain.transpose(y2,n),n,n);

            temp7=myClient.remotingBrain.AddConst(myClient.remotingBrain.transpose(y2,n),temp4[0],n);

            temp8=myClient.remotingBrain.MultiplyMatrix(temp6,temp2,n,n,n,n);

            temp9=myClient.remotingBrain.MultiplyMatrix(temp8,y2,n,n,n,1);

            temp10=myClient.remotingBrain.SumMatrix(temp5,temp9,n,1);

            x=myClient.remotingBrain.MultiplyRowOnMatrix(temp7,temp10,n,1);

            System.out.println("x= ");
            myClient.PrintRow(x,1);
            final double endTime = System.currentTimeMillis();
            System.out.println("Total execution time: " + (endTime - startTime)/1000 +" seconds");
        }
    }
    public static void main(String[] args) throws Exception
    {
        new lab_4();
    }
}
