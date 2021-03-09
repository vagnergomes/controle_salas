package br.com.controlesalas.controllers;

import br.com.controlesalas.entities.Agendamento;
import br.com.controlesalas.entities.Sala;
import br.com.controlesalas.services.AgendamentoService;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.servlet.http.HttpSession;

import org.primefaces.component.timeline.TimelineUpdater;

import org.primefaces.event.timeline.TimelineModificationEvent;
import org.primefaces.model.timeline.TimelineEvent;
import org.primefaces.model.timeline.TimelineModel;

@Named
@RequestScoped
public class customTimelineView implements Serializable {

    @Inject
    private AgendamentoService service;
    private Agendamento agendamento = new Agendamento();
    private List<Agendamento> agendamentos = new ArrayList<>();

    private Sala sala;

    private TimelineModel model;
    private TimelineEvent event; // current changed event  
    private List<TimelineEvent> overlappedOrders; // all overlapped orders (events) to the changed order (event)  
    private List<TimelineEvent> ordersToMerge; // selected orders (events) in the dialog which should be merged  
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime start;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime end;

    Date data = new Date(System.currentTimeMillis());
    Calendar c = Calendar.getInstance();
    SimpleDateFormat format = new SimpleDateFormat("HH:mm");

    @PostConstruct
    protected void initialize() {
        c.setTime(data);
        c.add(Calendar.HOUR, -2);
        LocalDateTime ldt_start = c.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        start = ldt_start;

        c.setTime(data);
        c.add(Calendar.HOUR, 2);
        LocalDateTime ldt_end = c.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        end = ldt_end;

        // create timeline model  
        sala = null;
        model = new TimelineModel();

    }

    public void read2() {
        agendamento = new Agendamento();
        Long idSala = sala.getIdSala();
        Object obj = 1;
        agendamentos = service.todosSala(idSala);

        List<TimelineEvent> evs = new ArrayList();
        for (Agendamento ag : agendamentos) {
            String titulo = ag.getTitulo() + " <br/>" + format.format(ag.getInicio()) + "-" + format.format(ag.getFim());
            TimelineEvent t = new TimelineEvent();
            t.setData(titulo);
            t.setStartDate(converteDateToDateTime(ag.getInicio()));
            t.setEndDate(converteDateToDateTime(ag.getFim()));
            t.setEditable(Boolean.FALSE);
            t.setGroup(ag.getSala().getNome_sala());
            evs.add(t);
        }

        // iterate over groups  
        for (int j = 1; j <= 1; j++) {
            for (TimelineEvent t : evs) {
                model.add(t);
            }
        }
    }

    public void selecionar(Sala s) {
        getSession().removeAttribute("idSala");
        getSession().setAttribute("idSala", s.getIdSala());
    }

    public TimelineModel read() {
//         Long idSala = s.getIdSala();
//        Object obj = idSala;
        Long idSala = (Long) getSession().getAttribute("idSala");
        //Long idSala = (Long) obj;
        agendamentos = service.todosSala(idSala);
        List<TimelineEvent> evs = new ArrayList();
        for (Agendamento ag : agendamentos) {
            String titulo = ag.getTitulo() + " <br/>" + format.format(ag.getInicio()) + "-" + format.format(ag.getFim());
            TimelineEvent t = new TimelineEvent();
            t.setData(titulo);
            t.setStartDate(converteDateToDateTime(ag.getInicio()));
            t.setEndDate(converteDateToDateTime(ag.getFim()));
            t.setEditable(Boolean.FALSE);
            t.setGroup(ag.getSala().getNome_sala());
            evs.add(t);
        }

        // iterate over groups  
        for (int j = 1; j <= 1; j++) {
            for (TimelineEvent t : evs) {
                model.add(t);
            }
        }

        return model;
    }

    public LocalDateTime converteDateToDateTime(Date dateToConvert) {
        return Instant.ofEpochMilli(dateToConvert.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public static Long convertToLong(Object o) {
        String stringToConvert = String.valueOf(o);
        Long convertedLong = Long.parseLong(stringToConvert);
        return convertedLong;
    }

    public TimelineModel getModel() {
        return model;
    }

    public void onChange(TimelineModificationEvent e) {
        // get changed event and update the model  
        event = e.getTimelineEvent();
        model.update(event);
        ordersToMerge = null;
    }

    public void onDelete(TimelineModificationEvent e) {
        // keep the model up-to-date  
        model.delete(e.getTimelineEvent());
    }

    public void merge() {
        // merge orders and update UI if the user selected some orders to be merged  
        if (ordersToMerge != null && !ordersToMerge.isEmpty()) {
            model.merge(event, ordersToMerge, TimelineUpdater.getCurrentInstance(":mainForm:timeline"));
        } else {
            FacesMessage msg
                    = new FacesMessage(FacesMessage.SEVERITY_INFO, "Nothing to merge, please choose orders to be merged", null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }

        overlappedOrders = null;
        ordersToMerge = null;
    }

    public int getSelectedOrder() {
        if (event == null) {
            return 0;
        }

        return ((Order) event.getData()).getNumber();
    }

    public List<TimelineEvent> getOverlappedOrders() {
        return overlappedOrders;
    }

    public List<TimelineEvent> getOrdersToMerge() {
        return ordersToMerge;
    }

    public void setOrdersToMerge(List<TimelineEvent> ordersToMerge) {
        this.ordersToMerge = ordersToMerge;
    }

    public TimelineEvent getEvent() {
        return event;
    }

    public void setEvent(TimelineEvent event) {
        this.event = event;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Sala getSala() {
        return sala;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
    }

    public HttpSession getSession() {
        return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    }

}
