package com.jahnelgroup.queue.hazelcast.reliable;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class ReliableMessage implements Serializable {

    @Id
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

    public Long getId() {
        return id;
    }

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
