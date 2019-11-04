the program using stream to read to json file.
With keyword : I make a stream of lines of string and looking for keyword in the string.
Then save those lines matched keyword in to JSONObject List. 
Then we look into JSONObject which has title contains keywords and print out the mactched title.

.For References : I make a list of string which has all the id of references,
Again using stream to read json file to file matching id and save that to JSONObject. Print out the title of the matched ID Object.

A recursion to looks into JSONOBject to obtain list of Reference and search again. 
A cap for recursion function is k - 1.

