/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import rs.etf.sab.operations.*;
import rs.etf.sab.tests.TestHandler;
import rs.etf.sab.tests.TestRunner;
//import test.*;

/**
 *
 * @author Aleksa
 * 
 * Pre pokretnja zameniti mesta IdG i IdO u tabeli Opstina jer inace nece raditi!!!
 * Isto ovo za IdP i IdK u AktuelnaVoznja
 * 
 */
public class StudentMain {
    public static void main(String[] args) {
        CityOperations cityOperations = new sa200355_CityOperations();
        DistrictOperations districtOperations = new sa200355_DistrictOperations();
        CourierOperations courierOperations = new sa200355_CourierOperations();
        CourierRequestOperation courierRequestOperation = new sa200355_CourierRequestOperation();
        GeneralOperations generalOperations = new sa200355_GeneralOperations();
        UserOperations userOperations = new sa200355_UserOperations();
        VehicleOperations vehicleOperations = new sa200355_VehicleOperations();
        PackageOperations packageOperations = new sa200355_PackageOperations();

        TestHandler.createInstance(
                cityOperations,
                courierOperations,
                courierRequestOperation,
                districtOperations,
                generalOperations,
                userOperations,
                vehicleOperations,
                packageOperations);


        TestRunner.runTests();
    }
}
