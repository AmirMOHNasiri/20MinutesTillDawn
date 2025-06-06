package com.game.Model.Player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.game.Model.CollisionRect;
import com.game.Model.Gun.Gun;
import com.game.Model.Manage.HibernateUtil;
import jakarta.persistence.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

@Entity
@Table(name = "players")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String username;

    private String password;
    @Enumerated(EnumType.STRING)
    private Avatar avatar;
    @Enumerated(EnumType.STRING)
    private Question question;

    private String answer;

    private Integer score;

    private Integer kills;

    private float mostTimeAlive;

    private int maxHp;
    private int currentHp;
    private int speed;

    private boolean isPlayerIdle;

    @Embedded
    private Gun gun;

    private Vector2 position;

    @Embedded
    private CollisionRect rect;

    @Column(name = "custom_avatar_path", nullable = true)
    private String customAvatarPath;

    private boolean isInvincible;
    private float invincibleTimer;

    private Integer level;
    private Integer currentXp;
    private Integer xpForNextLevel;

    public Player() {}

    public Player(String username, String password, Avatar avatar, Question question, String answer) {
        this.username = username;
        this.password = password;
        this.avatar = avatar;
        this.question = question;
        this.answer = answer;
        this.score = 0;
        this.kills = 0;
        this.mostTimeAlive = 0;
        this.isInvincible = false;
        this.invincibleTimer = 0f;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getCurrentXp() {
        return currentXp;
    }

    public void addXp(int amount) {
        this.currentXp += amount;
        Gdx.app.log("XP", "Gained " + amount + " XP. Total: " + currentXp + "/" + xpForNextLevel);

        while (currentXp >= xpForNextLevel) {
            levelUp();
        }
    }

    private void levelUp() {
        this.level++;
        this.currentXp -= this.xpForNextLevel;
        this.xpForNextLevel = 20 * this.level;
        Gdx.app.log("LEVEL UP!", "Reached Level " + this.level + "! Next level at " + this.xpForNextLevel + " XP.");
        // TODO: اعمال آپگریدهای مربوط به لول آپ
    }

    public void setCurrentXp(Integer currentXp) {
        this.currentXp = currentXp;
    }

    public Integer getXpForNextLevel() {
        return xpForNextLevel;
    }

    public void setXpForNextLevel(Integer xpForNextLevel) {
        this.xpForNextLevel = xpForNextLevel;
    }

    public static boolean isValidPassword(String password, Label label) {
        if (password.length() < 8) {
            label.setText("Password must be at least 8 characters");
            return false;
        }
        if (!password.matches(".*[_()*&%$#@].*")) {
            label.setText("Password must contain special characters");
            return false;
        }
        if (!password.matches(".*\\d.*")) {
            label.setText("Password must contain numbers");
            return false;
        }
        if (!password.matches(".*[A-Z].*")){
            label.setText("Password must contain capital letters");
            return false;
        }
        return true;
    }

    public void save() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.merge(this);
            tx.commit();
        } catch (Exception e){
            if (tx != null) tx.rollback();
            System.err.println("Failed to save player");
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public String getUsername() {
        return username;
    }

    public static List<Player> getAllPlayers() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Player> players = session.createQuery("FROM Player", Player.class).list();
        session.close();
        return players;
    }

    public static Player getPlayerByUsername(String username){
        for (Player player : getAllPlayers()) {
            if (player.getUsername().equals(username))
                return player;
        }
        return null;
    }

    public String getPassword() {
        return password;
    }

    public Question getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public String getScore() {
        return String.valueOf(score);
    }

    public Integer getScoreAsInteger() {return score;}

    public Integer getKills() {return kills;}

    public Float getMostTimeAlive() {return mostTimeAlive;}

    public void increaseScore(int score) {
        this.score += score;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public int getCurrentHp() {
        return currentHp;
    }

    public void setCurrentHp(int currentHp) {
        this.currentHp = currentHp;
    }

    public void takeDamage(int damage) {
        if (isInvincible) return;

        this.currentHp -= damage;
        if (this.currentHp <= 0) this.currentHp = 0;

        this.isInvincible = true;
        this.invincibleTimer = 1.5f;
    }

    public void update(float delta) {
        if (isInvincible) {
            invincibleTimer -= delta;
            if (invincibleTimer <= 0f) {
                isInvincible = false;
            }
        }
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public Gun getGun() {
        return gun;
    }

    public void setGun(Gun gun) {
        this.gun = gun;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public boolean isPlayerIdle() {
        return isPlayerIdle;
    }

    public void setPlayerIdle(boolean playerIdle) {
        isPlayerIdle = playerIdle;
    }

    public CollisionRect getRect() {
        return rect;
    }

    public void setRect(CollisionRect rect) {
        this.rect = rect;
    }

    public boolean isInvincible() {
        return isInvincible;
    }

    public float getInvincibleTimer() {
        return invincibleTimer;
    }

    public void delete() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.remove(this);
            tx.commit();
        } catch (Exception e){
            if (tx != null) tx.rollback();
            System.err.println("Failed to delete player: " + this.username);
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public String getCustomAvatarPath() {
        return customAvatarPath;
    }

    public void setCustomAvatarPath(String customAvatarPath) {
        this.customAvatarPath = customAvatarPath;
    }
}
