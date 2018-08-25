/*
RORIVE Olivier | VERWIMP Jim
2302
Réseaux et technologies Internet : étape 6
Date de dernière mise à jour : le 12/01/2018 à 18h52
*/

package application_billets;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.Vector;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import pay.ReponsePAY;
import pay.RequetePAY;
import pay.UtilePAY;
import pay_classes.Payment;
import tickmap.KeyExchangeTICKMAP;
import tickmap.ReponseTICKMAP;
import tickmap.RequeteTICKMAP;
import static tickmap.UtileTICKMAP.CODE_PROVIDER;
import tickmap.UtileTICKMAP;
import static tickmap.UtileTICKMAP.HMACAlg;
import static tickmap.UtileTICKMAP.asymmetricAlg;
import static tickmap.UtileTICKMAP.symmetricAlg;
import tickmap_classes.Agent;
import tickmap_classes.Reservation;
import tickmap_classes.Vol;

public class Main_Frame extends javax.swing.JFrame
{
    private final String filename = "config.properties";
    public final String keyStorePath = "D:\\GitHub\\ReseauSecondSess\\Etape 6\\Certificats\\keystore_client";
    private final String certificatPaymentPath = "D:\\GitHub\\ReseauSecondSess\\Etape 6\\Certificats\\certificat_serveur_payment.cer";
    
    private final Login_Frame login_frame;
    private int PORT_BILLETS;
    private String ADRESSE_BILLETS;
    private Socket cliSock;
    
    private Agent agent;
    
    private Key privateKey;
    private PublicKey publicKey;
    private X509Certificate certificat;
    
    private SecretKey cleChiffrement;
    private SecretKey cleHMAC;
    
    private final DefaultTableModel dtm;
    private List<Vol> listeVols;
    
    private X509Certificate certificat_serveur_payment;
    private String ADRESSE_PAYMENT;
    private int PORT_PAYMENT;
    
    public Main_Frame()
    {
        initComponents();
        //LireFichierProperties(filename);
        
        ADRESSE_BILLETS = "192.168.0.19";
        PORT_BILLETS = 30016;
        ADRESSE_PAYMENT = "192.168.0.19";
        PORT_PAYMENT = 30017;
        
        login_frame = new Login_Frame(this);
        login_frame.setVisible(true);
        
        dtm = (DefaultTableModel) JTVols.getModel();
        
        ReadPaymentCertificate(certificatPaymentPath);
    }
    
    private void LireFichierProperties(String filename)
    {
        Properties properties = null;
        InputStream input = null;
        
        properties = new Properties();
        try 
        {
            input = new FileInputStream(filename);
            properties.load(input);  
            
            if(properties.getProperty("ADRESSE_BILLETS") != null)
            {
                ADRESSE_BILLETS = properties.getProperty("ADRESSE_BILLETS");
            }
            else
            {
                System.err.println("L'adresse du serveur billets est introuvable dans le fichier " + filename);
                System.exit(1);
            }
            
            if(properties.getProperty("PORT_BILLETS") != null)
            {
                PORT_BILLETS = Integer.parseInt(properties.getProperty("PORT_BILLETS"));
            }
            else
            {
                System.err.println("Le numéro de port billets est introuvable dans le fichier " + filename);
                System.exit(1);
            }
            
            if(properties.getProperty("ADRESSE_PAYMENT") != null)
            {
                ADRESSE_PAYMENT = properties.getProperty("ADRESSE_PAYMENT");
            }
            else
            {
                System.err.println("L'adresse du serveur payment est introuvable dans le fichier " + filename);
                System.exit(1);
            }
            
            if(properties.getProperty("PORT_PAYMENT") != null)
            {
                PORT_PAYMENT = Integer.parseInt(properties.getProperty("PORT_PAYMENT"));
            }
            else
            {
                System.err.println("Le numéro de port payment est introuvable dans le fichier " + filename);
                System.exit(1);
            }
        } 
        catch (FileNotFoundException fnfeEx) 
        {
            try
            {
                FileOutputStream fos = new FileOutputStream(filename);
                
                properties.put("ADRESSE_BILLETS", "192.168.0.19");
                ADRESSE_BILLETS = "192.168.0.19";
                properties.put("PORT_BILLETS", "30016");
                PORT_BILLETS = 30016;
                properties.put("ADRESSE_PAYMENT", "192.168.0.19");
                ADRESSE_PAYMENT = "192.168.0.19";
                properties.put("PORT_PAYMENT", "30017");
                PORT_PAYMENT = 30017;
                
                try 
                {
                    properties.store(fos, null);
                }
                catch (IOException ioEx) 
                {
                    System.err.println("Erreur IO lors de l'écriture du fichier "  + filename + " : " + ioEx);
                    System.exit(0);
                }
            } 
            catch (FileNotFoundException fnfeEx2)
            {
                System.err.println("Erreur FileNotFound lors de l'écriture du fichier " + filename + " : " + fnfeEx2);
                System.exit(0);
            }
        } 
        catch (IOException ex) 
        {
            System.err.println("Erreur IO lors de la lecture du fichier " + filename + " : " + ex);
            System.exit(0);
        }
    }
    
    public void initSocket(String adresse, int port)
    {
        cliSock = null;
        try
        {
            cliSock = new Socket(adresse, port);
        }
        catch (UnknownHostException e)
        { 
            System.err.println("Erreur ! Host non trouvé [" + e + "]"); 
        }
        catch (IOException e)
        { 
            System.err.println("Erreur ! Pas de connexion ? [" + e + "]"); 
        }
    }
    
    public void readKeyStore(String keyStorePath)
    {
        try
        {
            KeyStore ks = KeyStore.getInstance("PKCS12", "BC");
            ks.load(new FileInputStream(keyStorePath),"client123".toCharArray());
            Enumeration en = ks.aliases();
            String aliasCourant = null;
            Vector vectaliases = new Vector();
            while (en.hasMoreElements()) vectaliases.add(en.nextElement());
                Object[] aliases = vectaliases.toArray();
            //OU : String[] aliases = (String []) (vectaliases.toArray(new String[0]));
           
            for (int i = 0; i < aliases.length; i++)
            {
                aliasCourant = (String)aliases[i];
                setCertificat((X509Certificate)ks.getCertificate(aliasCourant));
                setPrivateKey((PrivateKey)ks.getKey(aliases[i].toString(), "client123".toCharArray()));
                setPublicKey(getCertificat().getPublicKey());
            }
        }
        catch (Exception e)
        {
            System.err.println("Impossible de lire le keyStore à l'emplacement : " + keyStorePath);
            System.err.println("[" + e.getMessage() + "]");
            System.exit(1);
        }
    }
    
    public Object sendReceive(Object requete, String adresse, int port)
    {
        initSocket(adresse, port);

        boolean retour = UtileTICKMAP.sendObject(cliSock, requete);
        if(retour == false)
        {
            System.err.println("Erreur lors d'un envoi de message.");
            return null;
        }
        else
        {
            Object reponse = null;
            Object object = UtileTICKMAP.receiveObject(cliSock);
            if(object instanceof ReponseTICKMAP)
            {
                reponse = (ReponseTICKMAP) object;
            }
            else
            {
                if(object instanceof ReponsePAY)
                {
                    reponse = (ReponsePAY) object;
                }
                else
                {
                    if(object instanceof SealedObject)
                    {
                        SealedObject sealedObject = (SealedObject) object;
                        String algorithm = sealedObject.getAlgorithm();
                        Cipher cipher = null;
                        //if("RSA/ECB/PKCS1Padding".equals(algorithm))
                        if(asymmetricAlg.equals(algorithm))
                        {
                            cipher = UtileTICKMAP.initCipher(asymmetricAlg, CODE_PROVIDER, Cipher.DECRYPT_MODE, getPrivateKey());
                        }
                        else
                        {
                            cipher = UtileTICKMAP.initCipher(symmetricAlg, CODE_PROVIDER, Cipher.DECRYPT_MODE, getCleChiffrement());
                        }
                        try
                        {
                            reponse = sealedObject.getObject(cipher);
                        } 
                        catch (IOException | ClassNotFoundException | IllegalBlockSizeException | BadPaddingException ex)
                        {
                            System.err.println("Erreur lors de la lecture de l'objet reçu par le serveur : " + ex.getMessage());
                        }
                    }
                }
            }
            
            return reponse;
        }
    }
    
    public void sendCertificateToServeurBillets()
    {
        RequeteTICKMAP requete = new RequeteTICKMAP(RequeteTICKMAP.SEND_CLIENT_CERTIFICATE, getCertificat());
        ReponseTICKMAP reponse = (ReponseTICKMAP) sendReceive(requete, ADRESSE_BILLETS, PORT_BILLETS);

        if(reponse != null && reponse.getCode() == ReponseTICKMAP.SEND_SERVER_CERTIFICATE)
        {
            try
            {
                reponse.getCertificatServeur().verify(reponse.getCertificatServeur().getPublicKey());  
                reponse.getCertificatServeur().checkValidity();
                RequeteTICKMAP requeteCles = new RequeteTICKMAP(RequeteTICKMAP.REQUEST_KEY_EXCHANGE, getCertificat());                        
                KeyExchangeTICKMAP reponseCles = (KeyExchangeTICKMAP) sendReceive(requeteCles, ADRESSE_BILLETS, PORT_BILLETS);
                
                if(reponseCles != null && reponseCles.getCode() == KeyExchangeTICKMAP.SEND_KEYS_OK)
                {
                    setCleChiffrement(reponseCles.getCleChiffrement());
                    setCleHMAC(reponseCles.getCleHMAC());
                    
                    RecupererListeVols();
                }
                else
                {
                    System.err.println("L'échange de clé ne s'est pas bien passé !");
                    System.exit(1);
                }
            } 
            catch (CertificateException | NoSuchAlgorithmException | InvalidKeyException | NoSuchProviderException | SignatureException ex)
            {
                System.err.println("Exception lors de la vérification du certificat du serveur : " + ex.getMessage());
                System.exit(1);
            }
        }
        else
        {
            if(reponse != null && reponse.getCode() == ReponseTICKMAP.CLIENT_CERTIFICAT_CHECK_ERROR)
            {
                System.err.println("Le certificat envoyé par le client a été refusé par le serveur.");
                System.exit(1);
            }
            else
            {
                System.err.println("Arrêt du client pour cause de code réponse inconnu.");
                System.exit(1);
            }
        }
    }
    
    private void RecupererListeVols()
    {
        RequeteTICKMAP requeteVols = new RequeteTICKMAP(RequeteTICKMAP.REQUEST_FLIGHTS);
        ReponseTICKMAP reponseVols = (ReponseTICKMAP) sendReceive(requeteVols, ADRESSE_BILLETS, PORT_BILLETS);

        if(reponseVols != null && reponseVols.getCode() == ReponseTICKMAP.REQUEST_FLIGHTS_OK)
        {
            setListeVols(reponseVols.getListeVols());
            RemplirTableauVols();
        }
        else
        {
            System.err.println("Une erreur est survenue lors de la récupération de la liste des vols...");
            System.exit(1);
        }
    }
    
    private void RemplirTableauVols()
    {
        dtm.setRowCount(0);
        
        ((DefaultTableCellRenderer)JTVols.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        
        for(int i = 0 ; i < getListeVols().size() ; i++)
        {
            Vol vol = getListeVols().get(i);
            Object[] tabObjet = new Object[Vol.class.getDeclaredFields().length];
            tabObjet[0] = vol.getNumVol();
            tabObjet[1] = vol.getDestination();
            tabObjet[2] = vol.getZone();
            tabObjet[3] = vol.getNombrePlaces();
            tabObjet[4] = vol.getPlacesRestantes();
            tabObjet[5] = vol.getDistance();
            String depart = sdf.format(vol.getHeureDepart());
            tabObjet[6] = depart;
            String arrivee = sdf.format(vol.getHeureArrivee());
            tabObjet[7] = arrivee;
            tabObjet[8] = vol.getPrixParPersonne();
            tabObjet[9] = vol.getNumAvion();
            dtm.addRow(tabObjet);
            for(int j = 0 ; j < tabObjet.length ; j++)
            {
                JTVols.getColumnModel().getColumn(j).setCellRenderer(centerRenderer);
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jScrollPane1 = new javax.swing.JScrollPane();
        JTVols = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Application Billets");

        JTVols.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Vol", "Destination", "Zone", "Nombre de places", "Places restantes", "Distance", "Départ", "Arrivée", "Par personne (€)", "Avion"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Double.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean []
            {
                false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex)
            {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        JTVols.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                JTVolsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(JTVols);
        if (JTVols.getColumnModel().getColumnCount() > 0)
        {
            JTVols.getColumnModel().getColumn(0).setPreferredWidth(15);
            JTVols.getColumnModel().getColumn(1).setPreferredWidth(45);
            JTVols.getColumnModel().getColumn(2).setPreferredWidth(20);
            JTVols.getColumnModel().getColumn(3).setPreferredWidth(50);
            JTVols.getColumnModel().getColumn(4).setPreferredWidth(50);
            JTVols.getColumnModel().getColumn(5).setPreferredWidth(30);
            JTVols.getColumnModel().getColumn(6).setPreferredWidth(30);
            JTVols.getColumnModel().getColumn(7).setPreferredWidth(30);
            JTVols.getColumnModel().getColumn(9).setPreferredWidth(15);
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 872, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void JTVolsMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_JTVolsMouseClicked
    {//GEN-HEADEREND:event_JTVolsMouseClicked
        if(evt.getClickCount() == 2)
        {
            Vol vol = getListeVols().get(JTVols.getSelectedRow());
            
            Client_Dialog client_dialog = new Client_Dialog(this,true, getAgent(), vol);
            client_dialog.setVisible(true);
            client_dialog.setModal(true);
            if(client_dialog.valider == true)
            {
                RequeteTICKMAP requete = new RequeteTICKMAP(RequeteTICKMAP.ADD_TRAVELER, client_dialog.client);
                Cipher cipherE = UtileTICKMAP.initCipher(symmetricAlg, CODE_PROVIDER, Cipher.ENCRYPT_MODE, cleChiffrement);
                SealedObject sealedObject = UtileTICKMAP.createSealedObject(requete, cipherE);
                ReponseTICKMAP reponse = (ReponseTICKMAP) sendReceive(sealedObject, ADRESSE_BILLETS, PORT_BILLETS);

                client_dialog.dispose();
                if(reponse != null && reponse.getCode() == ReponseTICKMAP.REQUEST_ADD_TRAVELER_OK)
                {
                    Accompagnants_Dialog accompagnants_dialog = new Accompagnants_Dialog(this, true, getAgent(), vol, reponse.getClient(), cliSock, cleChiffrement);
                    accompagnants_dialog.setVisible(true);
                    accompagnants_dialog.setModal(true);
                    if(accompagnants_dialog.valider == true)
                    {
                        Reservation reservation = accompagnants_dialog.reservation;
                        accompagnants_dialog.dispose();
                        RequeteTICKMAP requeteReservation = new RequeteTICKMAP(RequeteTICKMAP.REQUEST_RESERVATION, reservation);
                        Cipher cipherERes = UtileTICKMAP.initCipher(symmetricAlg, CODE_PROVIDER, Cipher.ENCRYPT_MODE, cleChiffrement);
                        SealedObject sealedObjectReservation = UtileTICKMAP.createSealedObject(requeteReservation, cipherERes);
                        ReponseTICKMAP reponseReservation = (ReponseTICKMAP) sendReceive(sealedObjectReservation, ADRESSE_BILLETS, PORT_BILLETS);
                        
                        if(reponseReservation != null && reponseReservation.getCode() == ReponseTICKMAP.REQUEST_RESERVATION_OK)
                        {
                            String message = String.valueOf(reponseReservation.getListeNumerosPlaces().get(0));
                            for(int i = 1 ; i < reponseReservation.getListeNumerosPlaces().size() ; i++)
                            {
                                message += ", " + reponseReservation.getListeNumerosPlaces().get(i); 
                            }
                            int reply = JOptionPane.showConfirmDialog(null, "Prix à payer : " + reponseReservation.getPrixReservation() + " Euros | Numéros des places : " + message, "Confirmer ?", JOptionPane.YES_NO_OPTION);
                            
                            Mac hmac;
                            byte[] hb;
                            try 
                            {
                                hmac = Mac.getInstance(HMACAlg, CODE_PROVIDER);
                                hmac.init(cleHMAC);
                                hmac.update(agent.toString().getBytes());
                                hb = hmac.doFinal();
                            } 
                            catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeyException ex) 
                            {
                                System.err.println("Erreur lors de la conception du HMAC de l'agent : " + ex.getMessage());
                                hb = null;
                            }
                            
                            if(hb != null)
                            {
                                RequeteTICKMAP requeteConfirmation;
                                if (reply == JOptionPane.YES_OPTION) 
                                {
                                    requeteConfirmation = new RequeteTICKMAP(RequeteTICKMAP.RESERVATION_AGREEMENT, agent.getNumAgent(), hb);
                                }
                                else 
                                {
                                    requeteConfirmation = new RequeteTICKMAP(RequeteTICKMAP.RESERVATION_DISAGREEMENT, agent.getNumAgent(), 
                                            hb, reservation.getVol().getNumVol(), reponseReservation.getListeNumerosPlaces());
                                }
                                
                                ReponseTICKMAP reponseConfirmation = (ReponseTICKMAP) sendReceive(requeteConfirmation, ADRESSE_BILLETS, PORT_BILLETS);
                                
                                if(reponseConfirmation != null)
                                {
                                    JOptionPane.showMessageDialog(null,reponseConfirmation.getMessage(),"Information",JOptionPane.INFORMATION_MESSAGE);
                                    
                                    if("La commande a bien été confirmée.".equals(reponseConfirmation.getMessage()))
                                    {
                                        Payment_Dialog payment_dialog = new Payment_Dialog(this, true);
                                        payment_dialog.setVisible(true);
                                        payment_dialog.setModal(true);

                                        Signature s;
                                        byte[] signature = null;
                                        try
                                        {
                                            s = Signature.getInstance(UtilePAY.signatureAlg, UtilePAY.CODE_PROVIDER);
                                            s.initSign((PrivateKey)getPrivateKey());
                                            s.update(agent.toString().getBytes());
                                            signature = s.sign();
                                        } 
                                        catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeyException | SignatureException ex)
                                        {
                                            System.err.println("Erreur lors de la confection de la signature : " + ex.getMessage());
                                        }

                                        if(signature != null)
                                        {
                                            Payment payment = new Payment();
                                            payment.setNumClient(reservation.getClient().getNumClient());
                                            payment.setProprietaireCarte(payment_dialog.proprietaire);
                                            Cipher chiffrement;
                                            byte[] crypte;
                                            try 
                                            {
                                                chiffrement = Cipher.getInstance(asymmetricAlg, CODE_PROVIDER);
                                                chiffrement.init(Cipher.ENCRYPT_MODE, certificat_serveur_payment.getPublicKey());
                                                crypte = chiffrement.doFinal(payment_dialog.numCarte.getBytes());
                                                payment.setNumeroCarte(crypte);
                                            } 
                                            catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) 
                                            {
                                                System.err.println("Erreur lors du cryptage du numéro de carte : " + ex.getMessage());
                                            }
                                            payment.setMontant(reponseReservation.getPrixReservation());
                                            payment.setSignature(signature);
                                            payment.setAdresseMail(payment_dialog.adresseMail);
                                            payment_dialog.dispose();
                                            
                                            RequetePAY requetePaiement = new RequetePAY(RequetePAY.REQUEST_PAYMENT, agent.toString().getBytes(), getCertificat(), payment);
                                            System.out.println("Adresse payment : " + ADRESSE_PAYMENT);
                                            ReponsePAY reponsePaiement = (ReponsePAY) sendReceive(requetePaiement, ADRESSE_PAYMENT, PORT_PAYMENT);

                                            if(reponsePaiement != null && reponsePaiement.getCode() == ReponsePAY.REQUEST_PAYMENT_OK)
                                            {
                                                RequeteTICKMAP requeteValidation = new RequeteTICKMAP(RequeteTICKMAP.RESERVATION_VALIDATION, reservation, reponseReservation.getListeNumerosPlaces());
                                                Cipher cipherEVal = UtileTICKMAP.initCipher(symmetricAlg, CODE_PROVIDER, Cipher.ENCRYPT_MODE, cleChiffrement);
                                                SealedObject sealedObjectValidation = UtileTICKMAP.createSealedObject(requeteValidation, cipherEVal);
                                                ReponseTICKMAP reponseValidation = (ReponseTICKMAP) sendReceive(sealedObjectValidation, ADRESSE_BILLETS, PORT_BILLETS);

                                                if(reponseValidation != null && reponseValidation.getCode() == ReponseTICKMAP.RESERVATION_VALIDATION_OK)
                                                {
                                                    JOptionPane.showMessageDialog(null,"La réservation a été validée avec succès. Tout est en ordre !","Information",JOptionPane.INFORMATION_MESSAGE);
                                                }
                                                else
                                                {
                                                    JOptionPane.showMessageDialog(null,"Une erreur est survenue lors de la validation de la réservation.","Erreur",JOptionPane.ERROR_MESSAGE);
                                                }
                                            }
                                            else
                                            {
                                                JOptionPane.showMessageDialog(null,"Une erreur est survenue lors du paiement.","Erreur",JOptionPane.ERROR_MESSAGE);
                                            }
                                        }
                                        else
                                        {
                                            JOptionPane.showMessageDialog(null,"Une erreur est survenue.","Erreur",JOptionPane.ERROR_MESSAGE);
                                        }
                                    }
                                }
                                else
                                {
                                    JOptionPane.showMessageDialog(null,"Une erreur est survenue.","Erreur",JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        }
                        else
                        {
                            if(reponseReservation != null && reponseReservation.getCode() == ReponseTICKMAP.REQUEST_RESERVATION_ERROR)
                            {
                                JOptionPane.showMessageDialog(null,"Une erreur est survenue lors de la réservation.","Erreur",JOptionPane.ERROR_MESSAGE);
                            }
                            else
                            {
                                System.err.println("Erreur inattendue lors de la réservation des billets pour le client...");
                            }
                        }
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(null,"L'opération a été annulée.","Information",JOptionPane.INFORMATION_MESSAGE);
                    }
                }
                else
                {
                    if(reponse != null && reponse.getCode() == ReponseTICKMAP.REQUEST_ADD_TRAVELER_ERROR)
                    {
                        JOptionPane.showMessageDialog(null,"Le client saisi n'existe pas.","Erreur",JOptionPane.ERROR_MESSAGE);
                    }
                    else
                    {
                        System.err.println("Erreur inattendue lors de la réception de la réponse du serveur pour le client...");
                    }
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null,"L'opération a été annulée.","Information",JOptionPane.INFORMATION_MESSAGE);
            }
            RecupererListeVols();
        }
    }//GEN-LAST:event_JTVolsMouseClicked
    
    private void ReadPaymentCertificate(String path)
    {
        InputStream inStream = null;
        try
        {
            inStream = new FileInputStream(path);
        }
        catch (FileNotFoundException e)
        {
            System.out.println("Fichier certificat : " + e.getMessage());
        }
        
        CertificateFactory cf = null;
        try
        {
            cf = CertificateFactory.getInstance("X.509");
            certificat_serveur_payment = (X509Certificate)cf.generateCertificate(inStream);
        }
        catch (CertificateException ex)
        {
            System.err.println("Erreur lors de l'instanciation du certificat du serveur payment : " + ex.getMessage());
            System.exit(1);
        }
        
        try 
        { 
            if(inStream != null)
                inStream.close(); 
        }
        catch (FileNotFoundException ex)
        { 
            System.err.println("Erreur FNFE lors de la fermeture du fichier : " + ex.getMessage()); 
            System.exit(1);
        }
        catch (IOException ex)
        { 
            System.err.println("Erreur IO lors de la fermeture du fichier : " + ex.getMessage());
            System.exit(1);
        }
        
        try
        {
            certificat_serveur_payment.verify(certificat_serveur_payment.getPublicKey());
        } 
        catch (CertificateException | NoSuchAlgorithmException | InvalidKeyException | NoSuchProviderException | SignatureException ex)
        {
            System.err.println("Le certificat du serveur payment récupéré n'est pas vérifiable : " + ex.getMessage());
            System.exit(1);
        }
        
        try
        {
            certificat_serveur_payment.checkValidity();
        } 
        catch (CertificateExpiredException | CertificateNotYetValidException ex)
        {
            System.err.println("Le certificat du serveur payment récupéré n'est pas valide : " + ex.getMessage());
            System.exit(1);
        }
    }
    
    public static void main(String args[])
    {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try
        {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
            {
                if ("Nimbus".equals(info.getName()))
                {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex)
        {
            java.util.logging.Logger.getLogger(Main_Frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(Main_Frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(Main_Frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(Main_Frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                new Main_Frame().setVisible(false);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable JTVols;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the ADRESSE_BILLETS
     */
    public String getAdresseBillets()
    {
        return ADRESSE_BILLETS;
    }
    
    /**
     * @return the PORT_BILLETS
     */
    public int getPortBillets()
    {
        return PORT_BILLETS;
    }
    
    /**
     * @return the ADRESSE_PAYMENT
     */
    public String getAdressePayment()
    {
        return ADRESSE_PAYMENT;
    }
    
    /**
     * @return the PORT_PAYMENT
     */
    public int getPortPayment()
    {
        return PORT_PAYMENT;
    }
    
    /**
     * @return the privateKey
     */
    public Key getPrivateKey()
    {
        return privateKey;
    }

    /**
     * @param privateKey the privateKey to set
     */
    public void setPrivateKey(Key privateKey)
    {
        this.privateKey = privateKey;
    }

    /**
     * @return the publicKey
     */
    public PublicKey getPublicKey()
    {
        return publicKey;
    }

    /**
     * @param publicKey the publicKey to set
     */
    public void setPublicKey(PublicKey publicKey)
    {
        this.publicKey = publicKey;
    }

    /**
     * @return the certificat
     */
    public X509Certificate getCertificat()
    {
        return certificat;
    }

    /**
     * @param certificat the certificat to set
     */
    public void setCertificat(X509Certificate certificat)
    {
        this.certificat = certificat;
    }
    
    /**
     * @return the agent
     */
    public Agent getAgent()
    {
        return agent;
    }

    /**
     * @param agent the agent to set
     */
    public void setAgent(Agent agent)
    {
        this.agent = agent;
    }

    /**
     * @return the cleChiffrement
     */
    public SecretKey getCleChiffrement()
    {
        return cleChiffrement;
    }

    /**
     * @param cleChiffrement the cleChiffrement to set
     */
    public void setCleChiffrement(SecretKey cleChiffrement)
    {
        this.cleChiffrement = cleChiffrement;
    }

    /**
     * @return the cleHMAC
     */
    public SecretKey getCleHMAC()
    {
        return cleHMAC;
    }

    /**
     * @param cleHMAC the cleHMAC to set
     */
    public void setCleHMAC(SecretKey cleHMAC)
    {
        this.cleHMAC = cleHMAC;
    }

    /**
     * @return the listeVols
     */
    public List<Vol> getListeVols()
    {
        return listeVols;
    }

    /**
     * @param listeVols the listeVols to set
     */
    public void setListeVols(List<Vol> listeVols)
    {
        this.listeVols = listeVols;
    }
}
