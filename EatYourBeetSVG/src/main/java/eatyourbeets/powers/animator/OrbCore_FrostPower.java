package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import eatyourbeets.utilities.GameActionsHelper_Legacy;
import eatyourbeets.cards.animator.OrbCore_Frost;

public class OrbCore_FrostPower extends OrbCore_AbstractPower
{
    public static final String POWER_ID = CreateFullID(OrbCore_FrostPower.class.getSimpleName());

    public OrbCore_FrostPower(AbstractCreature owner, int amount)
    {
        super(POWER_ID, owner, amount);

        this.value = OrbCore_Frost.VALUE;

        updateDescription();
    }

    @Override
    protected void OnSynergy(AbstractPlayer p, AbstractCard usedCard)
    {
        GameActionsHelper_Legacy.ApplyPower(p, p, new PlatedArmorPower(p, value), value);
    }
}


//            AbstractMonster target = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
//            if (target != null)
//            {
//                AbstractDungeon.actionManager.addToBottom(new SFXAction("ATTACK_MAGIC_BEAM_SHORT", 0.5F));
//                AbstractDungeon.actionManager.addToBottom(new VFXAction(new BorderFlashEffect(Color.SKY)));
//
//                if (Settings.FAST_MODE)
//                {
//                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new SmallLaserEffect(target.hb.cX, target.hb.cY, owner.hb.cX, owner.hb.cY), 0.1F));
//                }
//                else
//                {
//                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new SmallLaserEffect(target.hb.cX, target.hb.cY, owner.hb.cX, owner.hb.cY), 0.3F));
//                }
//
//                AbstractDungeon.actionManager.addToBottom(new DamageAction(target, new DamageInfo(owner, this.amount, DamageInfo.DamageType.THORNS)));
//
//                this.flash();
//                this.amount += growth;
//                updateDescription();
//            }