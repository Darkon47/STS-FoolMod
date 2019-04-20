package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ModifyDamageAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.Utilities;
import eatyourbeets.cards.AnimatorCard_Cooldown;
import eatyourbeets.cards.Synergies;

public class DolaSchwi extends AnimatorCard_Cooldown
{
    public static final String ID = CreateFullID(DolaSchwi.class.getSimpleName());

    public DolaSchwi()
    {
        super(ID, 2, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);

        Initialize(8,0,1);

        this.baseSecondaryValue = this.secondaryValue = 3;

        SetSynergy(Synergies.NoGameNoLife);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        VulnerablePower power = new VulnerablePower(m, this.magicNumber, false);
        GameActionsHelper.Callback(new ApplyPowerAction(m, p, power, this.magicNumber), this::OnActionCompleted, this);

//        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, power, this.magicNumber));
//        int damage = this.damage;
//        if (!m.hasPower(VulnerablePower.POWER_ID) && !m.hasPower(ArtifactPower.POWER_ID))
//        {
//            damage = (int)power.atDamageReceive(damage, DamageInfo.DamageType.NORMAL);
//        }
//
//        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
//
//        if (ProgressCooldown())
//        {
//            OnCooldownCompleted(p, m);
//        }
    }

    private void OnActionCompleted(Object state, AbstractGameAction action)
    {
        ApplyPowerAction applyPowerAction = Utilities.SafeCast(action, ApplyPowerAction.class);
        if (state == this && applyPowerAction != null)
        {
            AbstractMonster m = (AbstractMonster) action.target;
            AbstractPlayer p = AbstractDungeon.player;

            this.calculateCardDamage((AbstractMonster) action.target);

            GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.SLASH_VERTICAL);

            //AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_VERTICAL));

            if (ProgressCooldown())
            {
                OnCooldownCompleted(p, m);
            }
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {          
            upgradeDamage(4);
            upgradeMagicNumber(1);
        }
    }

    @Override
    protected int GetBaseCooldown()
    {
        return 3;
    }

    @Override
    protected void OnCooldownCompleted(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.AddToBottom(new ModifyDamageAction(this.uuid, 30));
    }
}