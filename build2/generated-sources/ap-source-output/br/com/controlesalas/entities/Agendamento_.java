package br.com.controlesalas.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2020-07-07T16:39:55")
@StaticMetamodel(Agendamento.class)
public class Agendamento_ { 

    public static volatile SingularAttribute<Agendamento, String> sala;
    public static volatile SingularAttribute<Agendamento, Date> hora_inicio;
    public static volatile SingularAttribute<Agendamento, Date> hora_fim;
    public static volatile SingularAttribute<Agendamento, Long> idAgendamento;
    public static volatile SingularAttribute<Agendamento, Date> dia;
    public static volatile SingularAttribute<Agendamento, String> descricao;

}