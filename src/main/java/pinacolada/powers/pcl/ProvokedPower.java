package pinacolada.powers.pcl;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import pinacolada.effects.AttackEffects;
import pinacolada.interfaces.subscribers.OnMonsterMoveSubscriber;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLJUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class ProvokedPower extends PCLPower implements OnMonsterMoveSubscriber
{
    public static final int ATTACK_MULTIPLIER = 50;
    public static final String POWER_ID = CreateFullID(ProvokedPower.class);
    private byte moveByte;
    private AbstractMonster.Intent moveIntent;
    private EnemyMoveInfo move;
    protected int lastDamage;
    protected int lastMultiplier;
    protected boolean lastIsMultiDamage;

    public ProvokedPower(AbstractCreature owner)
    {
        super(owner, POWER_ID);

        Initialize(1, PowerType.DEBUFF, true);
    }

    @Override
    public void onRemove()
    {
        super.onRemove();
        PCLCombatStats.onMonsterMove.Unsubscribe(this);

        AbstractMonster m = PCLJUtils.SafeCast(this.owner, AbstractMonster.class);
        if (m != null && this.moveIntent != null) {
            m.setMove(this.moveByte, this.moveIntent, this.lastDamage, this.lastMultiplier, this.lastIsMultiDamage);
            m.createIntent();
            m.applyPowers();
        }

    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();
        PCLCombatStats.onMonsterMove.Subscribe(this);

        final AbstractMonster monster = PCLJUtils.SafeCast(owner, AbstractMonster.class);
        if (monster != null)
        {
            this.moveByte = monster.nextMove;
            this.moveIntent = monster.intent;
            PCLActions.Last.Callback(() -> {
                try {
                    Field f = AbstractMonster.class.getDeclaredField("move");
                    f.setAccessible(true);
                    this.move = (EnemyMoveInfo)f.get(monster);
                    this.lastDamage = this.move.baseDamage;
                    this.lastMultiplier = this.move.multiplier;
                    this.lastIsMultiDamage = this.move.isMultiDamage;
                    this.move.intent = AbstractMonster.Intent.ATTACK;
                    ArrayList<DamageInfo> damages = monster.damage;
                    if (damages == null || damages.isEmpty()) {
                        this.move.baseDamage = 1;
                    }
                    else {
                        this.move.baseDamage = damages.get(0).base;
                    }
                    monster.createIntent();
                } catch (NoSuchFieldException | IllegalAccessException var2) {
                    PCLJUtils.LogWarning(this, "Monster could not be provoked");
                }
            });
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        ReducePower(1);
    }

    @Override
    public void updateDescription() {
        this.description =  FormatDescription(0, ATTACK_MULTIPLIER);
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType type) {
        return type == DamageInfo.DamageType.NORMAL ? damage * (1 + (ATTACK_MULTIPLIER / 100f)) : damage;
    }

    @Override
    public boolean OnMonsterMove(AbstractMonster m) {
        ArrayList<DamageInfo> damages = m.damage;
        if (damages == null || damages.isEmpty()) {
            PCLActions.Bottom.DealDamage(m, AbstractDungeon.player, 1, DamageInfo.DamageType.NORMAL, AttackEffects.BLUNT_HEAVY);
        }
        else {
            PCLActions.Bottom.DealDamage(m, AbstractDungeon.player, m.damage.get(0).base, m.damage.get(0).type, AttackEffects.BLUNT_HEAVY);
        }
        return false;
    }
}