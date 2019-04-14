import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;

public class HelloWorld{

     public static void main(String []args){
        System.out.println("Hello World");
        ArrayList<Asset> list = new ArrayList<Asset>();
        Asset x = new Asset();
        x.record("Google,BOND,15,50,0.05");
        Asset y = new Asset();
        y.record("Vodafone,STOCK,15,50,0");
        list.add(x);
        System.out.println(x.getAssetType());
        
        List<String> input = Arrays.asList("Vodafone,STOCK,10,50,0|Google,STOCK,15,50,0|Microsoft,BOND,15,100,0.05:Vodafone,STOCK,15,50,0|Google,STOCK,10,50,0|Microsoft,BOND,15,100,0.05".split(":"));
        List<String> portfolioStrings = Arrays.asList(input.get(0).split("\\|"));
        Collections.sort(portfolioStrings);
        
        List<String> benchStrings = Arrays.asList(input.get(1).split("\\|"));
        Collections.sort(benchStrings);
        
        Basket Portfolio = new Basket();
        Basket Benchmark = new Basket();
        for (int i = 0; i < portfolioStrings.size(); i++){
            Asset portAsset = new Asset();
            portAsset.record(portfolioStrings.get(i));
            Portfolio.add(portAsset);
            
            Asset benchAsset = new Asset();
            benchAsset.record(benchStrings.get(i));
            Benchmark.add(benchAsset);
            
        }
        
        double Portmarkcap = Portfolio.totalmarketvalue();
        double Benchmarkcap = Benchmark.totalmarketvalue();
        
        for (int i = 0; i < Portfolio.totalitems(); i++) {
            // a particular asset can be accessed via Basket.get(i)
            Asset Passet = Portfolio.get(i);  // portfolio asset
            Asset Basset = Benchmark.get(i);  // benchmark asset
            double PortAssetmarketvalue = Passet.marketvalue();
            double ppercent = PortAssetmarketvalue / Portmarkcap;
            
            double BenchAssetmarketvalue = Basset.marketvalue();
            double bpercent = BenchAssetmarketvalue / Benchmarkcap;
            
            double pdiff = bpercent-ppercent;
            
            // the share change is computed differently for bonds or stocks
            if (Passet.getAssetType().equals("STOCK")) {
                double sharesChange = pdiff * Portmarkcap/Passet.getPrice();
                int SharesChange = (int) Math.round(sharesChange);
 
             if (SharesChange > 0) {System.out.println("BUY,"+Passet.getAssetName()+","+SharesChange);}
            else if (SharesChange < 0) {System.out.println("SELL,"+Passet.getAssetName()+","+(-1*SharesChange));}
            }
            else if (Passet.getAssetType().equals("BOND")) {
                double sharesChange = pdiff * Portmarkcap/(Passet.getPrice()*0.01);
                int SharesChange = (int) Math.round(sharesChange);
            
            if (SharesChange > 0) {System.out.println("BUY,"+Passet.getAssetName()+","+SharesChange);}
            else if (SharesChange < 0) {System.out.println("SELL,"+Passet.getAssetName()+","+(-1*SharesChange));}                
            }
            
        }
     }
}

class Asset {
    
  String Name;
  String AssetType;
  int Shares;
  int Price;
  double accruedint;
  
   Asset(){
    Name = "";
    AssetType = "";
    Shares = 0;
    Price = 0;
    accruedint = 0.0;
  
  }
  // method takes as argument a single string including asset types and converts it into an Asset object
  void record(String a) {
  
    List<String> items = Arrays.asList(a.split("\\,"));
    Name = items.get(0);
    AssetType = items.get(1);
    Shares = Integer.parseInt(items.get(2));
    Price = Integer.parseInt(items.get(3));
    accruedint = Double.parseDouble(items.get(4));
    
  }
  
  double marketvalue() {
    if (AssetType.equals("STOCK")){
      double markval = Shares * Price;
       return markval;
    	} 
    else if (AssetType.equals("BOND")){double markval = Shares * (Price + accruedint) * 0.01;
          return markval;
       }
    else {System.out.println("ERROR, neither stock or bond"); return 0;}
  }
 
 String getAssetType() {
     return AssetType;
 }
 String getAssetName() {
     return Name;
 }
 int getPrice() {
     return Price;
 }
}

class Basket extends Asset {
    ArrayList<Asset> basket;
    
    Basket() {
        basket = new ArrayList<Asset>(); 
    }
    
    void add(Asset xyz){
        basket.add(xyz);
    }
    double totalmarketvalue() {
        double tval = 0;
        for (Asset element: basket) {
            tval = tval + element.marketvalue();
        }
        return tval;
    }
    Asset get(int i) {
        return basket.get(i);
    }
    int totalitems() {
        return basket.size();
    }
}
