
package Project2b;
import java.sql.*;
import org.json.simple.*;

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
        
        try
        {
            /* Identify the Server */
            String server = ("jdbc:mysql://localhost/p2_test");
            String username = "root";
            String password = "teamone";
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(server, username, password);
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
            }
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
