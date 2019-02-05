package assignment3;

public class Building {

	OneBuilding data;
	Building older;
	Building same;
	Building younger;
	
	public Building(OneBuilding data){
		this.data = data;
		this.older = null;
		this.same = null;
		this.younger = null;
	}
	
	public String toString(){
		String result = this.data.toString() + "\n";
		if (this.older != null){
			result += "older than " + this.data.toString() + " :\n";
			result += this.older.toString();
		}
		if (this.same != null){
			result += "same age as " + this.data.toString() + " :\n";
			result += this.same.toString();
		}
		if (this.younger != null){
			result += "younger than " + this.data.toString() + " :\n";
			result += this.younger.toString();
		}
		return result;
	}
	
	public Building addBuilding (OneBuilding b){ // returns the root
		// end case
		if(this.data==null) {
			this.data = b;
			return this;
		}

		if(b.yearOfConstruction < this.data.yearOfConstruction) { // older
			if(this.older==null ) this.older = new Building(b);
			else this.older.addBuilding (b);
		} 
		else if(b.yearOfConstruction>this.data.yearOfConstruction) { // younger
				if(this.younger==null) this.younger = new Building (b);
				else this.younger.addBuilding (b);
		}
		else if(b.yearOfConstruction==this.data.yearOfConstruction) { // same
			//compare the heights
			if(b.height>this.data.height) { // higher
				// swap roots
				OneBuilding buil = b;
				b = this.data;
				this.data = buil;
			}																// shorter or equal
			if(this.same==null) this.same = new Building (b);
			else this.same.addBuilding(b);
		}

		return this;
	}

	public Building addBuildings (Building b){
		if (b == null) return this;
		this.addBuilding(b.data);

		if (b.older != null) addBuildings(b.older);
		if (b.same != null) addBuildings(b.same);
		if (b.younger != null) addBuildings(b.younger);

		return this;
	}
	
	public Building removeBuilding (OneBuilding b){
		//if the data is not equals to b
		if (!this.data.equals(b)) {
			if (this.younger != null && b.yearOfConstruction > this.data.yearOfConstruction)
				this.younger = this.younger.removeBuilding(b);
			else if (this.older != null && b.yearOfConstruction < this.data.yearOfConstruction)
				this.older = this.older.removeBuilding(b);
			else if (this.same != null && b.yearOfConstruction == this.data.yearOfConstruction)
				this.same = this.same.removeBuilding(b);
		}
		else {
			if (this.older == null && this.younger == null && this.same == null) // branches are null, return null
				return null;
			if (this.same != null) {                    			 								   //if same is not null, add younger
				this.same.addBuildings(this.younger);
				this.same.addBuildings(this.older);
				return this.same;
			} 
			else if (this.older != null) {          														 //if older is not null, we're adding younger branch 
				this.older.addBuildings(this.younger);
				return this.older;
			} 
			else
				return this.younger;
		}

		return this;
	}
	
	public int oldest(){
		if(this.older==null) return this.data.yearOfConstruction;
		else return this.older.data.yearOfConstruction;
	}
	
	public int highest(){
		int height = this.data.height;

		if(this.older!=null)
			if(this.older.data.height>height)
				height = this.older.highest();
		if(this.same!=null)
			if(this.same.data.height>height)
				height = this.same.highest();
		if(this.younger!=null)
			if(this.younger.data.height>height)
				height = this.younger.highest();

		return height;
	}
	
	public OneBuilding highestFromYear (int year){
		if(this.data.yearOfConstruction<year) {
			if(this.younger!=null) return this.younger.highestFromYear(year);
			else return null;
		}
		if(this.data.yearOfConstruction>year) {
			if(this.older!=null) return this.older.highestFromYear(year);
			else return null;
		}
		return this.data;
	}
	
	public int numberFromYears (int yearMin, int yearMax){
		if(yearMin>yearMax) return 0;
		int counter = 0;
		if(this.data.yearOfConstruction>=yearMin && this.data.yearOfConstruction<=yearMax)
			++counter;
		if(this.older!=null)
			counter += this.older.numberFromYears(yearMin,yearMax);
		if(this.same!=null)
			counter += this.same.numberFromYears(yearMin,yearMax);
		if(this.younger!=null)
			counter += this.younger.numberFromYears(yearMin,yearMax);
		
		return counter;
	}
	
	public int[] costPlanning (int nbYears){
		int[]costs = new int[nbYears];
		for(int i=0; i<nbYears; i++)
			costs[i] = costOfYear(2018+i);
		return costs;
	}
	private int costOfYear(int year) {
		int cost = 0;

		if(this.data.yearForRepair==year)
			cost += this.data.costForRepair;
		if(this.older!=null)
			cost += this.older.costOfYear(year);
		if(this.same!=null)
			cost += this.same.costOfYear(year);
		if(this.younger!=null)
			cost += this.younger.costOfYear(year);

		return cost;
	}
}
