package vn.edu.hcmute.mp.g4mediaplayer.model.service;

import java.util.ArrayList;

public interface ServiceRepository<TEntity> {
    ArrayList<TEntity> getAll();
    TEntity getOne(Object... keys);
    void add(TEntity entity);
    void delete(Object... keys);
    void edit(TEntity oldEntity, TEntity newEntity);
}
