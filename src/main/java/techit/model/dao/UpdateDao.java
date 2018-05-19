package techit.model.dao;

import java.util.List;

import techit.model.Update;

public interface UpdateDao {

    Update getUpdate( Long id );

    List<Update> getUpdates();

    Update saveUpdate( Update update );

}
