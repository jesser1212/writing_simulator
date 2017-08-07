# writing_simulator
A program that simulates writing by combining user-specified elements of public domain works.

To run this program: 
1. Download the java files. 
2. Create a JSON formatted text file following the example given in the "ScannerGuide.txt" file provided in this repository, which follows this format* (you can have more categories than the 3 shown, and a variable amount of associated words after):

    {
    
      "Priorities": ["goal", "failure", "protagonist"],
      
      "LongBook": {
      
        "protagonist": ["thisHero", "Mr. thisHero"],	
        
        "goal": ["this goal", "the hero's goal", "a moral goal"] ,
        
        "failure": ["a failure", "the hero's fear", "the antagonist's victory"]
        

      },
      
      "ShortBook":{
      
        "protagonist": ["Jim Hawkins", "Jim", "Jimmy", "Hawkins"],	
        
        "goal": ["treasure", "the chest", "the gold", "rich"] ,
        
        "failure": ["mutiny", "death", "drown", "revolt", "defeat"]
        
      },

      "NewBook":{
      
        "protagonist": ["Quavo", "Migos HookMan", "Mr. Quavo"],	
        
        "goal": ["Takeoff flow", "Offset bars", "Quavo hooks"] ,
        
        "failure": ["not platinum-selling", "not performing with 2 Chainz"]
        
      }

    }
3. Download the text of public domain works** and put them in text files (the examples offered are in "TreasureIslandFull.txt" and "TheFinalProblemFullText.txt.").
4. Run the Main class's main function, following the execution with the command arguments [json file] [longbook text] [shortbook text] [name of new book] (for example, to run the files offered in this repository, one might run the program with this command: "./main ScannerGuide.txt TreasureIslandFull.txt TheFinalProblemFullText.txt TreasureIslandMixHolmes"). 


*This format demands that you have the fields "Priorities" (with an array of Strings) and "Longbook", "Shortbook", and "Newbook" (each a JSONObject containing several objects associated with an array of Strings). Make sure that you have the same categories for each of these books, and that the associated words match the order given (for example, a formal key term like "Mr. Hawkins" might be replaced in the newbook with another term that is also formal, e.g. "Mr. Smith" instead of "John"). <br />
**This program runs more effectively when you have one work that is longer than the other and enter it as the "longbook text" argument in step 4.
