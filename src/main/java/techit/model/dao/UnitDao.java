package techit.model.dao;

import java.util.List;

import techit.model.Unit;

public interface UnitDao {

    Unit getUnit( Long id );

    List<Unit> getUnits();

    Unit saveUnit( Unit unit );

}
