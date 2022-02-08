package pinacolada.cards.fool.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.fool.FoolCard;
import pinacolada.cards.fool.curse.Curse_GriefSeed;
import pinacolada.cards.fool.series.MadokaMagica.MadokaKaname;
import pinacolada.utilities.PCLActions;

public class MadokaKaname_Krimheild extends FoolCard
{
    public static final PCLCardData DATA = Register(MadokaKaname_Krimheild.class)
            .SetSkill(0, CardRarity.SPECIAL, PCLCardTarget.None)
            .SetSeries(MadokaKaname.DATA.Series)
            .PostInitialize(data -> data.AddPreview(new Curse_GriefSeed(), false));

    public MadokaKaname_Krimheild()
    {
        super(DATA);

        Initialize(0, 0, 4, 15);
        SetUpgrade(0, 0, 1, 0);
        SetPurge(true);

        SetAffinity_Blue(1);
        SetAffinity_Dark(1);

    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        PCLActions.Bottom.MakeCardInHand(new Curse_GriefSeed());
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainSorcery(magicNumber);
        if (info.TryActivateLimited()) {
            PCLActions.Bottom.GainWisdom(secondaryValue);
            PCLActions.Bottom.GainResistance(-3);
        }
    }
}
