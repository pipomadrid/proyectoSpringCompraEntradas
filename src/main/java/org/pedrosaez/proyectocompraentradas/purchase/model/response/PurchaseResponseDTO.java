package org.pedrosaez.proyectocompraentradas.purchase.model.response;


import java.io.Serializable;
import java.util.List;

public class PurchaseResponseDTO implements Serializable {

    private String timestamp;
    private String status;
    private String error;
    private List<String> message;
    private Info info;
    private  String infoadicional;


    public static class Info {
        private String nombreTitular;
        private String numeroTarjeta;
        private String mesCaducidad;
        private String yearCaducidad;
        private String cvv;
        private String emisor;
        private String concepto;
        private String cantidad;

        public String getNombreTitular() {
            return nombreTitular;
        }

        public void setNombreTitular(String nombreTitular) {
            this.nombreTitular = nombreTitular;
        }

        public String getNumeroTarjeta() {
            return numeroTarjeta;
        }

        public void setNumeroTarjeta(String numeroTarjeta) {
            this.numeroTarjeta = numeroTarjeta;
        }

        public String getMesCaducidad() {
            return mesCaducidad;
        }

        public void setMesCaducidad(String mesCaducidad) {
            this.mesCaducidad = mesCaducidad;
        }

        public String getYearCaducidad() {
            return yearCaducidad;
        }

        public void setYearCaducidad(String yearCaducidad) {
            this.yearCaducidad = yearCaducidad;
        }

        public String getCvv() {
            return cvv;
        }

        public void setCvv(String cvv) {
            this.cvv = cvv;
        }

        public String getEmisor() {
            return emisor;
        }

        public void setEmisor(String emisor) {
            this.emisor = emisor;
        }

        public String getConcepto() {
            return concepto;
        }

        public void setConcepto(String concepto) {
            this.concepto = concepto;
        }

        public String getCantidad() {
            return cantidad;
        }

        public void setCantidad(String cantidad) {
            this.cantidad = cantidad;
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<String> getMessage() {
        return message;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }

    public String getInfoadicional() {
        return infoadicional;
    }

    public void setInfoadicional(String infoadicional) {
        this.infoadicional = infoadicional;
    }


    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }
}
