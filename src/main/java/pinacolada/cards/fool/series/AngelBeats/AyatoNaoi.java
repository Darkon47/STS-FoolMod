package pinacolada.cards.fool.series.AngelBeats;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardEffectChoice;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.fool.FoolCard;
import pinacolada.effects.PCLEffekseerEFX;
import pinacolada.effects.SFX;
import pinacolada.effects.VFX;
import pinacolada.stances.pcl.WisdomStance;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class AyatoNaoi extends FoolCard
{
    public static final PCLCardData DATA = Register(AyatoNaoi.class).SetSkill(3, CardRarity.RARE, PCLCardTarget.None).SetSeriesFromClassPackage();
    private static final CardEffectChoice choices = new CardEffectChoice();

    public AyatoNaoi()
    {
        super(DATA);

        Initialize(0, 2, 2, 0);
        SetUpgrade(0, 0, 1, 0);

        SetAffinity_Blue(1, 0, 3);
        SetAffinity_Dark(1, 0, 0);
        SetExhaust(true);
        SetAfterlife(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);

        for (int i = 0; i < magicNumber; i++)
        {
            PCLActions.Bottom.ChannelOrb(new Dark()).AddCallback(o -> {
                if (o.size() > 0 && WisdomStance.IsActive()) {
                    PCLActions.Bottom.TriggerOrbPassive(o.get(0), 1);
                }
            });
        }

        if (!WisdomStance.IsActive()) {
            PCLActions.Bottom.ChangeStance(WisdomStance.STANCE_ID);
        }
    }

    @Override
    public void triggerOnAfterlife() {
        PCLActions.Bottom.Callback(() ->
        {
            int totalDamage = 0;
            for (AbstractMonster mo : PCLGameUtilities.GetEnemies(true))
            {
                totalDamage += PCLGameUtilities.GetPCLIntent(mo).GetDamage(true);
            }

            if (totalDamage > 0)
            {

                SFX.Play(SFX.DARKLING_REGROW_2);
                PCLActions.Bottom.VFX(VFX.EFX(PCLEffekseerEFX.SWORD25).SetScale(2f));
                PCLActions.Bottom.ApplyRippled(TargetHelper.Enemies(), totalDamage);
            }
        });
    }
}