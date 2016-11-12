package org.usfirst.frc862.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Properties;
import java.util.function.Consumer;

public class ConstantsBase {
    public String getFileName() {
        return "~/config.properties";
    }

    public String getResolvedFileName() {
        return getFileName().replaceFirst("^~", System.getProperty("user.home"));
    }

    public void withEachField(Consumer<Field> func) {
        for (Field field : this.getClass().getDeclaredFields()) {
            func.accept(field);
        }
    }

    public void withEachStaticField(Consumer<Field> func) {
        withEachField((Field f) -> {
            if (java.lang.reflect.Modifier.isStatic(f.getModifiers())) {
                func.accept(f);
            }
        });
    }

    public void writeToFile() {
        Properties prop = new Properties();
        OutputStream output = null;

        try {
            output = new FileOutputStream(getResolvedFileName());

            withEachStaticField((Field f) -> {
                String name = f.getName();
                Object value = null;
                try {
                    value = f.get(this);
                } catch (Exception e) {
                    System.err.println("Unable to get value for " + name);
                    e.printStackTrace();
                }

                prop.put(name, value.toString());
            });

            // save properties to project root folder
            prop.store(output, null);

        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void readFromFile() {
        Properties prop = new Properties();
        InputStream input = null;

        try {
            input = new FileInputStream(getResolvedFileName());
            prop.load(input);

            withEachStaticField((Field f) -> {
                String name = f.getName();
                String value = prop.getProperty(name);
                Class<?> klass = f.getType();
                Object o;
                try {
                    System.out.println("Loading " + name);
                    o = f.get(this);

                    if (value == null) {
                        // do nothing
                    } else if (o instanceof String) {
                        f.set(this, value);
                    } else if (o instanceof Double) {
                        f.setDouble(this, Double.parseDouble(value));
                    } else if (o instanceof Float) {
                        f.setFloat(this, Float.parseFloat(value));
                    } else if (o instanceof Boolean) {
                        f.setBoolean(this, Boolean.parseBoolean(value));
                    } else if (o instanceof Long) {
                        f.setLong(this, Long.parseLong(value));
                    } else if (o instanceof Integer) {
                        f.setInt(this, Integer.parseInt(value));
                    } else if (o instanceof Short) {
                        f.setShort(this, Short.parseShort(value));
                    } else if (o instanceof Byte) {
                        f.setByte(this, Byte.parseByte(value));
                    } else {
                        System.err.println("Unknown type: " + klass);
                    }
//                } catch (FileNotFoundException e) {
//                    writeToFile();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            });

        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
