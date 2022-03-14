package pinacolada.cards.fool.series.GenshinImpact;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.fool.FoolCard;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.PCLEffekseerEFX;
import pinacolada.effects.VFX;
import pinacolada.powers.fool.SwirledPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class JeanGunnhildr extends FoolCard
{
    public static final PCLCardData DATA = Register(JeanGunnhildr.class).SetAttack(1, CardRarity.RARE).SetSeriesFromClassPackage();

    public JeanGunnhildr()
    {
        super(DATA);

        Initialize(8, 1, 2, 1);
        SetUpgrade(3, 1, 0);
        SetAffinity_Red(1, 0, 0);
        SetAffinity_Green(1, 0, 1);
        SetAffinity_Light(1, 0, 1);

        SetLoyal(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.VFX(VFX.EFX(PCLEffekseerEFX.SWORD06, m.hb));
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_DIAGONAL);
        
    }

    @Override
    public void triggerOnManualDiscard()
    {
        if (CombatStats.TryActivateSemiLimited(cardID)) {
            for (AbstractMonster m : PCLGameUtilities.GetEnemies(true)) {
                PCLActions.Delayed.StackPower(player, new SwirledPower(m, 1));
            }
        }
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DiscardFromHand(name, magicNumber, false)
                .SetOptions(true, true, true)
                .AddCallback(cards ->
                {
                    int discarded = cards.size();
                    if (discarded > 0)
                    {
                        for (AbstractPower power : player.powers)
                        {
                            if (PCLGameUtilities.IsCommonDebuff(power))
                            {
                                int decrease = Math.min(discarded, power.amount);
                                PCLActions.Top.ReducePower(power, decrease);
                                discarded -= decrease;
                                if (discarded <= 0)
                                {
                                    break;
                                }
                            }
                        }
                    }
                });
    }
}