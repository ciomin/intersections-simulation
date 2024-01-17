package Team_martomfibut;

import Components.*;
import DataObjects.DataCar;
import DataObjects.DataCarQueue;
import DataObjects.DataString;
import DataObjects.DataTransfer;
import DataOnly.TransferOperation;
import Enumerations.LogicConnector;
import Enumerations.TransitionCondition;
import Enumerations.TransitionOperation;

public class Intersection1 {
    public static void main(String[] args) {

        PetriNet pn = new PetriNet();
        pn.PetriNetName = "Intersection1";

        pn.NetworkPort = 1080;

        DataString green = new DataString();
        green.Printable = false;
        green.SetName("green");
        green.SetValue("green");
        pn.ConstantPlaceList.add(green);

        DataString full = new DataString();
        full.Printable = false;
        full.SetName("full");
        full.SetValue("full");
        pn.ConstantPlaceList.add(full);

        // -------------------------------------------------------------------
        // -------------------------------Lane1--------------------------------
        // --------------------------------------------------------------------

        DataCar p1 = new DataCar();
        p1.SetName("P_a1");
        pn.PlaceList.add(p1);

        DataCarQueue p2 = new DataCarQueue();
        p2.Value.Size = 3;
        p2.SetName("P_x1");
        pn.PlaceList.add(p2);

        DataString p3 = new DataString();
        p3.SetName("P_TL1");
        pn.PlaceList.add(p3);

        DataCar p4 = new DataCar();
        p4.SetName("P_b1");
        pn.PlaceList.add(p4);

        DataTransfer OP1 = new DataTransfer();
        OP1.SetName("OP1");
        OP1.Value = new TransferOperation("localhost", "1082", "in1");
        pn.PlaceList.add(OP1);

        // -------------------------------------------------------------------------------------
        // --------------------------------Lane2-----------------------------------------------
        // -------------------------------------------------------------------------------------

        DataCar p5 = new DataCar(); //p5.Printable = false;
        p5.SetName("P_a2");
        pn.PlaceList.add(p5);

        DataCarQueue p6 = new DataCarQueue(); //p6.Printable = false;
        p6.Value.Size = 3;
        p6.SetName("P_x2");
        pn.PlaceList.add(p6);

        DataString p7 = new DataString(); //p7.Printable = false;
        p7.SetName("P_TL2");
        pn.PlaceList.add(p7);

        DataCar p8 = new DataCar(); //p8.Printable = false;
        p8.SetName("P_b2");
        pn.PlaceList.add(p8);

        DataTransfer OP2 = new DataTransfer();
        OP2.SetName("OP2");
        OP2.Value = new TransferOperation("localhost", "1082", "in2");
        pn.PlaceList.add(OP2);

        // ----------------------------------------------------------------------------
        // ----------------------------Exit lane 1-------------------------------------
        // ----------------------------------------------------------------------------

        DataCarQueue p9 = new DataCarQueue(); //p9.Printable = false;
        p9.Value.Size = 3;
        p9.SetName("P_o1");
        pn.PlaceList.add(p9);

        DataCar p10 = new DataCar(); //p10.Printable = false;
        p10.SetName("P_o1Exit");
        pn.PlaceList.add(p10);

        // ----------------------------------------------------------------------------
        // ----------------------------Exit lane 2-------------------------------------
        // ----------------------------------------------------------------------------

        DataCarQueue p11 = new DataCarQueue(); //p11.Printable = false;
        p11.Value.Size = 3;
        p11.SetName("P_o2");
        pn.PlaceList.add(p11);

        DataCar p12 = new DataCar(); //p12.Printable = false;
        p12.SetName("P_o2Exit");
        pn.PlaceList.add(p12);

        DataTransfer p14 = new DataTransfer();
        p14.SetName("PO3");
        p14.Value = new TransferOperation("localhost", "1081", "P_a3");
        pn.PlaceList.add(p14);

        // -------------------------------------------------------------------------------------------
        // --------------------------------Intersection-----------------------------------------------
        // -------------------------------------------------------------------------------------------

        DataCarQueue p13 = new DataCarQueue();
        p13.Value.Size = 1;
        p13.SetName("P_I");
        pn.PlaceList.add(p13);

        // T1 ------------------------------------------------
        PetriTransition t1 = new PetriTransition(pn);
        t1.TransitionName = "T_u1";
        t1.InputPlaceName.add("P_a1");
        t1.InputPlaceName.add("P_x1");

        Condition T1Ct1 = new Condition(t1, "P_a1", TransitionCondition.NotNull);
        Condition T1Ct2 = new Condition(t1, "P_x1", TransitionCondition.CanAddCars);
        T1Ct1.SetNextCondition(LogicConnector.AND, T1Ct2);

        GuardMapping grdT1 = new GuardMapping();
        grdT1.condition = T1Ct1;
        grdT1.Activations.add(new Activation(t1, "P_a1", TransitionOperation.AddElement, "P_x1"));
        t1.GuardMappingList.add(grdT1);

        Condition T1Ct3 = new Condition(t1, "P_a1", TransitionCondition.NotNull);
        Condition T1Ct4 = new Condition(t1, "P_x1", TransitionCondition.CanNotAddCars);
        T1Ct3.SetNextCondition(LogicConnector.AND, T1Ct4);

        GuardMapping grdT1_1 = new GuardMapping();
        grdT1_1.condition= T1Ct3;
        grdT1_1.Activations.add(new Activation(t1, "full", TransitionOperation.SendOverNetwork, "OP1"));
        grdT1_1.Activations.add(new Activation(t1, "P_a1", TransitionOperation.Move, "P_a1"));
        t1.GuardMappingList.add(grdT1_1);

        t1.Delay = 0;
        pn.Transitions.add(t1);

        // T2 ------------------------------------------------
        PetriTransition t2 = new PetriTransition(pn);
        t2.TransitionName = "T_e1";
        t2.InputPlaceName.add("P_x1");
        t2.InputPlaceName.add("P_TL1");

        Condition T2Ct1 = new Condition(t2, "P_TL1", TransitionCondition.Equal, "green");
        Condition T2Ct2 = new Condition(t2, "P_x1", TransitionCondition.HaveCar);
        T2Ct1.SetNextCondition(LogicConnector.AND, T2Ct2);

        GuardMapping grdT2 = new GuardMapping();
        grdT2.condition = T2Ct1;
        grdT2.Activations.add(new Activation(t2, "P_x1", TransitionOperation.PopElementWithoutTarget, "P_b1"));
        grdT2.Activations.add(new Activation(t2, "P_TL1", TransitionOperation.Move, "P_TL1"));

        t2.GuardMappingList.add(grdT2);

        t2.Delay = 0;
        pn.Transitions.add(t2);

        // T3 ------------------------------------------------
        PetriTransition t3 = new PetriTransition(pn);
        t3.TransitionName = "T_u2";
        t3.InputPlaceName.add("P_a2");
        t3.InputPlaceName.add("P_x2");

        Condition T3Ct1 = new Condition(t3, "P_a2", TransitionCondition.NotNull);
        Condition T3Ct2 = new Condition(t3, "P_x2", TransitionCondition.CanAddCars);
        T3Ct1.SetNextCondition(LogicConnector.AND, T3Ct2);

        GuardMapping grdT3 = new GuardMapping();
        grdT3.condition = T3Ct1;
        grdT3.Activations.add(new Activation(t3, "P_a2", TransitionOperation.AddElement, "P_x2"));
        t3.GuardMappingList.add(grdT3);

        Condition T3Ct3 = new Condition(t3, "P_a2", TransitionCondition.NotNull);
        Condition T3Ct4 = new Condition(t3, "P_x2", TransitionCondition.CanNotAddCars);
        T3Ct3.SetNextCondition(LogicConnector.AND, T3Ct4);

        GuardMapping grdT3_1 = new GuardMapping();
        grdT3_1.condition= T3Ct3;
        grdT3_1.Activations.add(new Activation(t3, "full", TransitionOperation.SendOverNetwork, "OP2"));
        grdT3_1.Activations.add(new Activation(t3, "P_a2", TransitionOperation.Move, "P_a2"));
        t3.GuardMappingList.add(grdT3_1);

        t3.Delay = 0;
        pn.Transitions.add(t3);

        // T4 ------------------------------------------------
        PetriTransition t4 = new PetriTransition(pn);
        t4.TransitionName = "T_e2";
        t4.InputPlaceName.add("P_x2");
        t4.InputPlaceName.add("P_TL2");

        Condition T4Ct1 = new Condition(t4, "P_TL2", TransitionCondition.Equal, "green");
        Condition T4Ct2 = new Condition(t4, "P_x2", TransitionCondition.HaveCar);
        T4Ct1.SetNextCondition(LogicConnector.AND, T4Ct2);

        GuardMapping grdT4 = new GuardMapping();
        grdT4.condition = T4Ct1;
        grdT4.Activations.add(new Activation(t4, "P_x2", TransitionOperation.PopElementWithoutTarget, "P_b2"));
        grdT4.Activations.add(new Activation(t4, "P_TL2", TransitionOperation.Move, "P_TL2"));
        t4.GuardMappingList.add(grdT2);

        t4.Delay = 0;
        pn.Transitions.add(t4);

        // T5----------------------------------------------------------------

        PetriTransition t5 = new PetriTransition(pn);
        t5.TransitionName = "T_g1Exit";
        t5.InputPlaceName.add("P_o1");

        Condition t5Ct1 = new Condition(t5, "P_o1", TransitionCondition.HaveCar);

        GuardMapping grdT5 = new GuardMapping();
        grdT5.condition = t5Ct1;
        grdT5.Activations.add(new Activation(t5, "P_o1", TransitionOperation.PopElementWithoutTarget, "P_o1Exit"));
        t5.GuardMappingList.add(grdT5);

        t5.Delay = 0;
        pn.Transitions.add(t5);

        // T6----------------------------------------------------------------

        PetriTransition t6 = new PetriTransition(pn);
        t6.TransitionName = "T_g2Exit";
        t6.InputPlaceName.add("P_o2");

        Condition t6Ct1 = new Condition(t6, "P_o2", TransitionCondition.HaveCar);

        GuardMapping grdT6 = new GuardMapping();
        grdT6.condition = t6Ct1;
        grdT6.Activations.add(new Activation(t6, "P_o2", TransitionOperation.PopElementWithoutTarget, "P_o2Exit"));
        t6.GuardMappingList.add(grdT6);

        t6.Delay = 0;
        pn.Transitions.add(t6);
        // --------------------------------------first part-------------------------------------------

        // T7 ------------------------------------------------
        PetriTransition t7 = new PetriTransition(pn);
        t7.TransitionName = "T_i1";
        t7.InputPlaceName.add("P_b1");
        t7.InputPlaceName.add("P_I");

        Condition t7Ct1 = new Condition(t7, "P_b1", TransitionCondition.NotNull);
        Condition t7Ct2 = new Condition(t7, "P_I", TransitionCondition.CanAddCars);
        t7Ct1.SetNextCondition(LogicConnector.AND, t7Ct2);

        GuardMapping grdT7 = new GuardMapping();
        grdT7.condition = t7Ct1;
        grdT7.Activations.add(new Activation(t7, "P_b1", TransitionOperation.AddElement, "P_I"));
        t7.GuardMappingList.add(grdT7);

        t7.Delay = 0;
        pn.Transitions.add(t7);

        // T8-----------------------------------------------------------
        PetriTransition t8 = new PetriTransition(pn);
        t8.TransitionName = "T_G1";
        t8.InputPlaceName.add("P_I");
        t8.InputPlaceName.add("P_o1");

        Condition t8Ct1 = new Condition(t8, "P_I", TransitionCondition.HaveCarForMe);
        Condition t8Ct2 = new Condition(t8, "P_o1", TransitionCondition.CanAddCars);
        t8Ct1.SetNextCondition(LogicConnector.AND, t8Ct2);

        GuardMapping grdT8 = new GuardMapping();
        grdT8.condition = t8Ct1;
        grdT8.Activations.add(new Activation(t8, "P_I", TransitionOperation.PopElementWithTargetToQueue, "P_o1"));
        t8.GuardMappingList.add(grdT8);

        t8.Delay = 0;
        pn.Transitions.add(t8);
        // ---------------------------------SecondPart-------------------------------------------

        // T9 ------------------------------------------------
        PetriTransition t9 = new PetriTransition(pn);
        t9.TransitionName = "T_i2";
        t9.InputPlaceName.add("P_b2");
        t9.InputPlaceName.add("P_I");

        Condition t9Ct1 = new Condition(t9, "P_b2", TransitionCondition.NotNull);
        Condition t9Ct2 = new Condition(t9, "P_I", TransitionCondition.CanAddCars);
        t9Ct1.SetNextCondition(LogicConnector.AND, t9Ct2);

        GuardMapping grdT9 = new GuardMapping();
        grdT9.condition = t9Ct1;
        grdT9.Activations.add(new Activation(t9, "P_b2", TransitionOperation.AddElement, "P_I"));
        t9.GuardMappingList.add(grdT9);

        t9.Delay = 0;
        pn.Transitions.add(t9);

        // T10-----------------------------------------------------------
        PetriTransition t10 = new PetriTransition(pn);
        t10.TransitionName = "T_G2";
        t10.InputPlaceName.add("P_I");
        t10.InputPlaceName.add("P_o2");

        Condition t10Ct1 = new Condition(t10, "P_I", TransitionCondition.HaveCarForMe);
        Condition t10Ct2 = new Condition(t10, "P_o2", TransitionCondition.CanAddCars);
        t10Ct1.SetNextCondition(LogicConnector.AND, t10Ct2);

        GuardMapping grdT10 = new GuardMapping();
        grdT10.condition = t10Ct1;
        grdT10.Activations.add(new Activation(t10, "P_I", TransitionOperation.PopElementWithTargetToQueue, "P_o2"));
        t10.GuardMappingList.add(grdT10);

        t10.Delay = 0;
        pn.Transitions.add(t10);

        // Tout-----------------------------------------------------------
        PetriTransition tout = new PetriTransition(pn);
        tout.TransitionName = "T_out";
        tout.InputPlaceName.add("P_o2Exit");

        Condition toutCt1 = new Condition(tout, "P_o2Exit", TransitionCondition.NotNull);

        GuardMapping grdTout = new GuardMapping();
        grdTout.condition = toutCt1;
        grdTout.Activations.add(new Activation(tout, "P_o2Exit", TransitionOperation.SendOverNetwork, "PO3"));
        tout.GuardMappingList.add(grdTout);

        tout.Delay = 0;
        pn.Transitions.add(tout);

        // -------------------------------------------------------------------------------------
        // ----------------------------PNStart-------------------------------------------------
        // -------------------------------------------------------------------------------------

        System.out.println("Intersection 1 started \n ------------------------------");
        pn.Delay = 2000;
        // pn.Start();

        PetriNetWindow frame = new PetriNetWindow(false);
        frame.petriNet = pn;
        frame.setVisible(true);

    }
}
