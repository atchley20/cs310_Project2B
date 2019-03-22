Tanner Atchley

Database to JSON Parser

This project is an individual project assigned during my Software Engineering class at Jacksonville State University. It involves using
the json-simple libraries, JSONObject, and JSONArray classes. The project is a simple program which retrieves data from a sample database
and parses it into an array of JSON objects. To complete this project the MySQL server first had to be added to our computers to connect to the 
database given by our professor. The database for this assignment is a simple list of twenty names and addresses, stored in a single table called "people".
For each record (or row) in the table, the program will retrieve the value from each field (or column), and store these values (with the exception of
the numeric id) in a new JSONObject, using the field (column) names as the key names.  These JSON objects will then be added to a JSONArray, in the same 
order in which the corresponding records appear in the database.  The JSONArray will then be returned to the caller. This beginning portion of code
is mainly initializing the variables and objects that will be used in the program's logic.


```java
public class Project2B 
{

    public JSONArray getJSONData() 
    {
        Connection conn = null;
        PreparedStatement pst_select = null;
        ResultSet resultset = null;
        ResultSetMetaData metadata = null;
        JSONObject data = new JSONObject();
        JSONArray final_result = new JSONArray();

        
        String query, key, value;
        boolean has_results;
        int results_count, column_count = 0;
```

The next section of code is identifying the server and connecting to it with a set username and password.

```java
        try
        {
            /* Identify the Server */
            String server = ("jdbc:mysql://localhost/p2_test");
            String username = "root";
            String password = "teamone";
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(server, username, password);
```

If the connection made to the server was successful, the program executes a query from the list of people in the database.
If the query is successful and there are results from the query within the has_results object, the data will be added to two ResultSet objects
called resultset and metadata and an integer that represents the number of columns.

```java
            if (conn.isValid(0)) 
            {
                has_results = pst_select.execute();
                query = "SELECT * FROM people";
                pst_select = conn.prepareStatement(query);

                while ( has_results || pst_select.getUpdateCount() != -1 ) 
                {

                    if ( has_results ) 
                    {
                        
                        resultset = pst_select.getResultSet();
                        metadata = resultset.getMetaData();
                        column_count = metadata.getColumnCount();
```

After the data has been retrieved and stored into these resultsets it needs to be parsed into an array of separate JSON objects. This 
begins with a while loop to ensure that there is still data left in the result set. Nested inside the while is a for loop that populates 
the values and keys from the query. Next, The final result with all of the keys and values stored is added to the data object. If there 
is not any data left in the resultset, it updates the results count. Lastly, the program attempts to get more results.

```java
                        while(resultset.next()) 
                        {
                            for (int i = 2; i <= column_count; i++) 
                            {
                                value = resultset.getString(i);
                                key = metadata.getColumnLabel(i);                                
                                data.put(key, value);
                            }
                            final_result.add(data.clone());
                        }
                    }
                    else 
                    {

                        results_count = pst_select.getUpdateCount();  

                        if ( results_count == -1 ) 
                        {
                            break;
                        }

                    }
                    has_results = pst_select.getMoreResults();
                }

```

The last portion of this project mainly involves working with the connection to the database and throwing error exceptions if the program 
receives an unexpected value. First, the connection is closed. Next the error message is prepared to be printed to the screen if there 
is an error in the input. Lastly, the program attempts to close each of the resultset objects and erase all data still stored in them.
If there is an error in doing this, there will also be an error message. The last line returns the parsed data from the database as a JSONArray called final_result. 

```java
            conn.close();
        }
        catch (Exception e) 
        {
            System.err.println(e.toString());
        }
        finally 
        {
            if (resultset != null) { try { resultset.close(); resultset = null; } catch (Exception e) {} }           
            if (pst_select != null) { try { pst_select.close(); pst_select = null; } catch (Exception e) {} }
        }
         return final_result;
    }
}
```
