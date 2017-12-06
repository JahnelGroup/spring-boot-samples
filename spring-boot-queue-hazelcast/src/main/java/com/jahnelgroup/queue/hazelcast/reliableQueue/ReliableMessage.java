package com.jahnelgroup.queue.hazelcast.reliableQueue;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * http://docs.hazelcast.org/docs/latest/manual/html-single/index.html#queue
 * http://docs.hazelcast.org/docs/latest/manual/html-single/index.html#queueing-with-persistent-datastore
 */
@Entity
public class ReliableMessage implements Serializable {

    @Id @GeneratedValue
    private Long id;
    private String message;

    public ReliableMessage(){

    }

    public ReliableMessage(Long id) {
        this.id = id;
    }

    public ReliableMessage(Long id, String message) {
        this.id = id;
        this.message = message;
    }

    public ReliableMessage(String message) {
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public String getMessage(){ return message; }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReliableMessage that = (ReliableMessage) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        return message != null ? message.equals(that.message) : that.message == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (message != null ? message.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ReliableMessage{" +
                "id=" + id +
                ", message='" + message + '\'' +
                '}';
    }
}
