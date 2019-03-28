package es.salenda.vertx.java.example;

public class Asterank {
    Float rms;
    String epoch;
    String readable_des;
    Float H;
    Integer num_obs;
    String ref;
    Float G;
    String last_obs;
    String comp;
    Float M;
    String U;
    Float e;
    Float a;
    Float om;
    String pert_p;
    Float d;
    Float i;
    String des;
    String flags;
    Integer num_opp;
    Float w;
    String pert_c;

    public Asterank(Float rms, String epoch, String readable_des, Float h, Integer num_obs, String ref, Float g, String last_obs, String comp, Float m, String u, Float e, Float a, Float om, String pert_p, Float d, Float i, String des, String flags, Integer num_opp, Float w, String pert_c) {
        this.rms = rms;
        this.epoch = epoch;
        this.readable_des = readable_des;
        H = h;
        this.num_obs = num_obs;
        this.ref = ref;
        G = g;
        this.last_obs = last_obs;
        this.comp = comp;
        M = m;
        U = u;
        this.e = e;
        this.a = a;
        this.om = om;
        this.pert_p = pert_p;
        this.d = d;
        this.i = i;
        this.des = des;
        this.flags = flags;
        this.num_opp = num_opp;
        this.w = w;
        this.pert_c = pert_c;
    }

    public Float getRms() {
        return rms;
    }

    public void setRms(Float rms) {
        this.rms = rms;
    }

    public String getEpoch() {
        return epoch;
    }

    public void setEpoch(String epoch) {
        this.epoch = epoch;
    }

    public String getReadable_des() {
        return readable_des;
    }

    public void setReadable_des(String readable_des) {
        this.readable_des = readable_des;
    }

    public Float getH() {
        return H;
    }

    public void setH(Float h) {
        H = h;
    }

    public Integer getNum_obs() {
        return num_obs;
    }

    public void setNum_obs(Integer num_obs) {
        this.num_obs = num_obs;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public Float getG() {
        return G;
    }

    public void setG(Float g) {
        G = g;
    }

    public String getLast_obs() {
        return last_obs;
    }

    public void setLast_obs(String last_obs) {
        this.last_obs = last_obs;
    }

    public String getComp() {
        return comp;
    }

    public void setComp(String comp) {
        this.comp = comp;
    }

    public Float getM() {
        return M;
    }

    public void setM(Float m) {
        M = m;
    }

    public String getU() {
        return U;
    }

    public void setU(String u) {
        U = u;
    }

    public Float getE() {
        return e;
    }

    public void setE(Float e) {
        this.e = e;
    }

    public Float getA() {
        return a;
    }

    public void setA(Float a) {
        this.a = a;
    }

    public Float getOm() {
        return om;
    }

    public void setOm(Float om) {
        this.om = om;
    }

    public String getPert_p() {
        return pert_p;
    }

    public void setPert_p(String pert_p) {
        this.pert_p = pert_p;
    }

    public Float getD() {
        return d;
    }

    public void setD(Float d) {
        this.d = d;
    }

    public Float getI() {
        return i;
    }

    public void setI(Float i) {
        this.i = i;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getFlags() {
        return flags;
    }

    public void setFlags(String flags) {
        this.flags = flags;
    }

    public Integer getNum_opp() {
        return num_opp;
    }

    public void setNum_opp(Integer num_opp) {
        this.num_opp = num_opp;
    }

    public Float getW() {
        return w;
    }

    public void setW(Float w) {
        this.w = w;
    }

    public String getPert_c() {
        return pert_c;
    }

    public void setPert_c(String pert_c) {
        this.pert_c = pert_c;
    }
}
