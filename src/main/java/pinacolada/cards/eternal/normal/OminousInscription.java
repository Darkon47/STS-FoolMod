package pinacolada.cards.eternal.normal;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.eternal.EternalCard;
import pinacolada.powers.EternalPower;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;

public class OminousInscription extends EternalCard
{
    public static final PCLCardData DATA = Register(OminousInscription.class)
            .SetSkill(1, CardRarity.UNCOMMON, PCLCardTarget.Normal);

    public OminousInscription()
    {
        super(DATA);

        Initialize(0, 0, 5, 1);
        SetUpgrade(0, 0, -1, 0);

        SetDark();

        SetEthereal(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.StackPower(new OminousInscriptionPower(m, secondaryValue));
        if (!CheckPrimaryCondition(true)) {
            PCLActions.Bottom.TakeDamage(magicNumber, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        }
    }

    public static class OminousInscriptionPower extends EternalPower
    {
        public OminousInscriptionPower(AbstractCreature owner, int amount)
        {
            super(owner, OminousInscription.DATA);

            Initialize(amount);
        }

        @Override
        public void updateDescription()
        {
            this.description = FormatDescription(0, amount);
        }

        public int onAttacked(DamageInfo info, int damageAmount) {
            if (info.owner != null && info.owner != this.owner) {
                PCLCombatStats.MatchingSystem.ResolveMeter.AddResolve(amount);
                flashWithoutSound();
            }

            return damageAmount;
        }
    }
}