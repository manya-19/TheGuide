package flint.durzo.theguide;

public class Adapter {

        private String city;

        public Adapter() {
        }

        public Adapter(String city) {
            this.city=city;
        }

        public static String getTitle() {
            return city;
        }

        public void setTitle(String name) {
            this.city = name;
        }

           }