package br.com.agrotis.core.domain;

public interface ModelView {

	static interface ID {};
    static interface INSERT {};
    static interface UPDATE {};
    static interface FIND extends ID {};
    static interface GET extends FIND {};    

}
