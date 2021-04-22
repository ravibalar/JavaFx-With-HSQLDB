    package model.db;

    import java.sql.Connection;
    import java.sql.DatabaseMetaData;
    import java.sql.ResultSet;
    import java.sql.Statement;

    public class InsertData {
        private Connection conn = null;

        public InsertData(){

        }
        public InsertData(Connection conn){
            this.conn = conn;
        }

        public boolean insert(String tableName,String insertStmt) throws Exception {
            try (Statement stmt = conn.createStatement()) {
                DatabaseMetaData dbm = conn.getMetaData();
                ResultSet tables = dbm.getTables(null, null, tableName.toUpperCase(), null);
                if (tables != null) {
                    if (tables.next()) {
                        // System.out.println("Table " + tableName + " exists.");
                        int result = stmt.executeUpdate(insertStmt);
                        System.out.println(result);
                        if (result > 0) {
                            System.out.println("Data has been inserted successfully");
                            tables.close();
                            return true;
                        } else {
                            // System.out.println("Row is not created");
                            //System.out.println(result);
                            throw new Exception("Row is not created");
                        }
                    } else {
                        System.out.println("Table " + tableName + " does not exist.");
                        throw new Exception("Table " + tableName + "does not exists.");
                    }
                } else {
                    System.out.println(("Problem with retrieving database metadata"));
                }
            } catch (Exception ex) {
                // System.out.println(ex.getMessage());
                throw ex;
            }finally {
                // tables.close();
            }
            return false;
        }
    }
